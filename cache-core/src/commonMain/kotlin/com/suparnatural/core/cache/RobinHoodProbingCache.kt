package com.suparnatural.core.cache

/**
 * A thread-safe [InMemoryCache] with a custom HashTable implementation using Robin Hood hashing.
 */
class RobinHoodProbingCache(
        size: Int,
        persistentStores: List<CacheStore> = emptyList(),
        replacementPolicy: CacheReplacementPolicy = FifoCacheReplacementPolicy(size)
) : InMemoryCache(size, persistentStores, replacementPolicy) {

    private fun <T : Cacheable> getObjectUnsafe(key: String): Triple<T?, Int, CacheableContainer?> {
        val hash = hashCode(key)
        var index = hash
        do {
            val targetObjDisplacement = computeDisplacement(index, hash)

            val existingObj = storage[index].value
            val existingObjDisplacement = if (existingObj == null) Int.MAX_VALUE else computeDisplacement(index, hashCode(existingObj.key))

            if (existingObj != null && existingObj.key == key) {
                return Triple(existingObj.obj as T, index, existingObj)
            }

            // return null if empty cell with positive displacement. If an entry existed, it must have been found
            // of if the existing obj has a larger displacement than target obj, then target obj is absent because it should have
            // been placed before the existing object since it has a lower displacement
            if ((existingObj != null && existingObjDisplacement > targetObjDisplacement)) {
                return Triple(null, -1, null)
            }
            index = (index + 1) % size
        } while (index != hash) // stop after complete wrap around
        return Triple(null, -1, null)
    }

    override fun <T : Cacheable> getObject(key: String): T? {
        // acquire reader lock
        try {
            lock.acquireReadLock()
            return getObjectUnsafe<T>(key).first
        } finally {
            lock.releaseReadLock()
        }
    }

    data class Quintuple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    private fun addObjectUnsafe(newObj: CacheableContainer, hash: Int, index: Int): Quintuple<CacheableContainer?, Int, Int, Boolean>? {

        val oldObj = storage[index].value
        var result: Quintuple<CacheableContainer?, Int, Int, Boolean>?
        if (oldObj == null || oldObj.key == newObj.key) {
            val casResult = storage[index].compareAndSet(oldObj, newObj)
            result = Quintuple(
                    if (!casResult) newObj else null, hash, index, casResult
            )
        } else {
            val oldObjHash = hashCode(oldObj.key)
            if (computeDisplacement(index, hash) < computeDisplacement(index, hashCode(oldObj.key))) {
                val casResult = storage[index].compareAndSet(oldObj, newObj)
                result = Quintuple(
                        if (!casResult) newObj else null, hash, index, casResult
                )
                if (result.fourth) {
                    result = Quintuple(oldObj, oldObjHash, oldObjHash, false)
                }
            } else {
                val nextIndex = (index + 1) % size
                if (nextIndex == hash) { // test wrap around
                    result = null
                } else {
                    result = Quintuple(newObj, hash, nextIndex, false)
                }
            }
        }
        return result

    }

    override fun <T : Cacheable> addObject(obj: T): Boolean {
        try {
            lock.acquireWriteLock()
            val hash = hashCode(obj.cacheKey())
            val container = CacheableContainer(obj)
            // create space
            super.addObject<T>(container)
            var result = addObjectUnsafe(container, hash, hash)
            while(result != null && !result.fourth) {
                result = addObjectUnsafe(result.first!!, result.second, result.third)
            }
            return result?.fourth ?: true
        } finally {
            lock.releaseWriteLock()
        }
    }

    private fun backShift(emptyCellIndex: Int) {
        var index = emptyCellIndex

        do {
            val nextIndex = (index + 1) % size
            // if cell is empty, return
            val obj = storage[nextIndex].value ?: return

            val hash = hashCode(obj.key)
            val displacement = computeDisplacement(nextIndex, hash)

            // if object already located in the ideal cell
            if (displacement == 0) return

            storage[index].value = obj
            storage[nextIndex].value = null
            index = nextIndex

        } while (index != emptyCellIndex)

    }


    override fun <T : Cacheable> removeObject(key: String): Pair<T?, Boolean> {
        try {
            lock.acquireWriteLock()
            val (obj, index, container) = getObjectUnsafe<T>(key)
            if (obj == null || container == null) return Pair(obj, false)

            super.removeObject<T>(container)

            if (!storage[index].compareAndSet(container, null)) return removeObject(key)
            backShift(index)

            return Pair(obj, true)
        } finally {
            lock.releaseWriteLock()
        }
    }

    private fun computeDisplacement(index: Int, hash: Int): Int {
        return if (index >= hash) index - hash
        else size - hash + index
    }

}