package com.suparnatural.core.cache

import com.suparnatural.core.threading.AtomicReference
import com.suparnatural.core.threading.ReadWriteLock
import kotlin.math.abs

/**
 * An abstract, thread-safe in-memory cache with a
 * custom HashTable implementation backed by an array of atomic values.
 * It guarantees consistency in both single and multi-threaded environment.
 */
abstract class InMemoryCache(
        override val size: Int,
        override val persistentStores: List<CacheStore> = emptyList(),
        override val replacementPolicy: CacheReplacementPolicy

) : Cache {

    /**
     * An array of fixed [size] which holds [Cacheable] objects.
     */
    internal var storage: Array<AtomicReference<CacheableContainer?>> = Array<AtomicReference<CacheableContainer?>>(size) { AtomicReference(null) }

    /**
     * A read write lock for thread safety.
     */
    protected val lock = ReadWriteLock()


    override fun hashCode(key: String): Int {
        val code = key.hashCode()
        val adjusted = if (code >= 0) code else abs(code)
        val wrapped = adjusted % size
        return if (code >= 0) wrapped else size - wrapped - 1
    }

    override fun getAllObjects(): List<Cacheable> {
        try {
            lock.acquireReadLock()
            return storage.filter {
                val value = it.value
                value != null && value.obj !is LinearProbingCache.TombStoneMarker
            }.mapNotNull {
                it.value?.obj
            }
        } finally {
            lock.releaseReadLock()
        }
    }

    /**
     * Adds an object to persistent store.
     * Assumes that lock is already obtained.
     */
    fun <T : Cacheable> addObject(obj: CacheableContainer): Boolean {
        persistentStores.forEach { storage ->
            storage.persistObject(obj.obj)
        }
        val removed = replacementPolicy.registerObjectUnsafe(obj)
        if (removed != null) {
            removeObject<Cacheable>(removed.key)
        }
        return true
    }

    /**
     * Removes the item from persistent stores.
     * Assumes that lock is already obtained.
     */
    fun <T : Cacheable> removeObject(obj: CacheableContainer): Pair<T?, Boolean> {
        persistentStores.forEach { storage ->
            storage.unlinkObject(obj.key)
        }
        replacementPolicy.unregisterObjectUnsafe(obj)
        return Pair(null, true)
    }

    override fun rehydrate() {
        try {
            lock.acquireWriteLock()
            storage.forEach {
                it.value = null
            }
            replacementPolicy.clearRegistryUnsafe()
            persistentStores.forEach {
                val objects = it.fetchAllObjects()
                it.wipe()
                objects.forEach { obj ->
                    addObject(obj)
                }
            }
        } finally {
            lock.releaseWriteLock()
        }
    }
}
