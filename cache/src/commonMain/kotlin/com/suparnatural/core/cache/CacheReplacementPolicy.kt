package com.suparnatural.core.cache

/**
 * A Cache Replacement policy maintains a registry of cached objects
 * to free space when cache is full. The object removed when cache
 * fills up depends on the implementation of [CacheReplacementPolicy.evictUnsafe] method.
 *
 * For example, [FifoCacheReplacementPolicy] replaces the first
 * cached object when cache runs out of space. Therefore, its [FifoCacheReplacementPolicy.evictUnsafe]
 * removes the oldest object from the registry and returns it.
 *
 * All methods are assumed to be thread unsafe and expect that the
 * caller has already acquired the locks.
 *
 * A custom replacement policy (Like LRU) can be implemented as long as
 * it conforms to this interface.
 *
 * ### Examples
 *
 * #### A custom cache replacement policy
 *
 * ```
 * class LRUCacheReplacementPolicy(override val cacheSize: Int): CacheReplacementPolicy {
 *     ...
 * }
 * ```
 */
interface CacheReplacementPolicy {

    /**
     * Size of the cache
     */
    val cacheSize: Int

    /**
     * Adds a cache entry for the object and returns the entry of a pruned object.
     * Pruning occurs when the cache space is full.
     */
    fun registerObjectUnsafe(obj: CacheableContainer): CacheableContainer?

    /**
     * Remove `obj` from cache registry.
     */
    fun unregisterObjectUnsafe(obj: CacheableContainer)

    /**
     * Prune the cache by 1 entry. Returns the removed object.
     * Call this method when cache runs out of space.
     */
    fun evictUnsafe(): CacheableContainer?

    /**
     * Clear any state and reset to initial. For example,
     * clearRegistryUnsafe any linked list state for LRU or FIFO cache.
     */
    fun clearRegistryUnsafe()
}