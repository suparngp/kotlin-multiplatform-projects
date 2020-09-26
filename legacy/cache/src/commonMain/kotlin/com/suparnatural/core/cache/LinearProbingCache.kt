package com.suparnatural.core.cache

/**
 * A thread-safe [InMemoryCache] with a custom HashTable implementation based on Linear Probing.
 */
class LinearProbingCache(
        size: Int,
        persistentStores: List<CacheStore> = emptyList(),
        replacementPolicy: CacheReplacementPolicy = FifoCacheReplacementPolicy(size)
) : InMemoryCache(size, persistentStores, replacementPolicy) {

    /**
     * A marker which replaces deleted objects.
     * If the space is marked as null after deletion, then
     * it leaves other objects with same hash codes unreachable.
     * Search continues if the current cell has a TombStoneMarker.
     */
    class TombStoneMarker(private val key: String) : Cacheable {
        override fun serializeForPersistence(): String {
            return key
        }

        override fun cacheKey(): String {
            return key
        }
    }

    private fun <T : Cacheable> getObjectUnsafe(key: String): Triple<T?, Int, CacheableContainer?> {
        val hash = hashCode(key)
        var index = hash
        do {
            // exit the probe because cell is empty
            val value = storage[index].value ?: return Triple(null, -1, null)

            // if the value is a deletion marker, skip to the next
            if (value.obj is TombStoneMarker) {
                index = (index + 1) % size
                continue
            }

            if (value.key == key) {
                @Suppress("UNCHECKED_CAST")
                return Triple(value.obj as T, index, value)
            }
            index = (index + 1) % size
        } while (index != hash)
        return Triple(null, -1, null)
    }

    override fun <T : Cacheable> getObject(key: String): T? {
        try {
            lock.acquireReadLock()
            return getObjectUnsafe<T>(key).first
        } finally {
            lock.releaseReadLock()
        }
    }

    override fun <T : Cacheable> addObject(obj: T): Boolean {
        try {
            lock.acquireWriteLock()
            val hash = hashCode(obj.cacheKey())
            var index = hash
            val container = CacheableContainer(obj)

            // check with replacement policy
            super.addObject<T>(container)
            var result = false
            do {
                val existing = storage[index].value
                when {
                    existing == null || existing.obj is TombStoneMarker || existing.key == container.key ->
                        result = storage[index].compareAndSet(existing, container)
                    else -> index = (index + 1) % size
                }

            } while (!result && index != hash)

            return result
        } finally {
            lock.releaseWriteLock()
        }

    }

    private fun <T : Cacheable> removeObjectUnsafe(key: String): Pair<T?, Boolean> {
        val (obj, index, container) = getObjectUnsafe<T>(key)

        // if obj not found return
        if (obj == null || container == null) return Pair(null, false)

        // update cache replacement policy
        super.removeObject<T>(container)

        // set the deletion marker
        if (!storage[index].compareAndSet(container, CacheableContainer(TombStoneMarker(key)))) return removeObjectUnsafe(key)

        return Pair(obj, true)
    }


    /**
     * @param T the type of object being removed by [key]
     */
    override fun <T : Cacheable> removeObject(key: String): Pair<T?, Boolean> {
        try {
            lock.acquireWriteLock()
            return removeObjectUnsafe<T>(key)
        } finally {
            lock.releaseWriteLock()
        }
    }
}
