package com.suparnatural.core.cache

/**
 * A generic Cache. A cache can be backed by persistent stores.
 * The main different between cache and a persistent store is that
 * the latter is considered to be slower than the former.
 * Therefore, the APIs to access objects from a persistent store are not
 * the same as that of a cache. For example, in [InMemoryCache] vs [DiskStore],
 * the [InMemoryCache] is the primary data structure which other parts
 * of the application should interact with while [DiskStore] can be used
 * to rehydrate the cache after a cold start.
 *
 * Therefore, avoid directly interacting with the store.
 *
 * However, you can always create your own cache which persists objects by default
 * without a backing store.
 *
 */
interface Cache {

    /**
     * Size of the cache
     */
    val size: Int
    /**
     * List of persistent stores backing cache.
     */
    val persistentStores: List<CacheStore>

    /**
     * Cache replacement policy to create space when cache is full.
     */
    val replacementPolicy: CacheReplacementPolicy

    /**
     * Returns a list of all the cached objects.
     */
    fun getAllObjects(): List<Cacheable>

    /**
     * Returns a object with the given [key].
     *
     * @param T the type of object being fetched.
     */
    fun <T : Cacheable> getObject(key: String): T?

    /**
     * Adds a [Cacheable] object to the cache. The cache key is retrieved
     * by calling [Cacheable.cacheKey] method.
     *
     * @param T the type of object being added.
     */
    fun <T : Cacheable> addObject(obj: T): Boolean

    /**
     * Removed a [Cacheable] object from the cache.
     *
     * @param T the type of object removed from [key]
     * @return A Pair of removed object and a boolean which is true if the object is removed.
     */
    fun <T : Cacheable> removeObject(key: String): Pair<T?, Boolean>

    /**
     * Returns a hash code for the given [key]. For example,
     * it may wrap the raw hash code for a string by cache sizeUnsafe.
     */
    fun hashCode(key: String): Int

    /**
     * Rehydrates the cache by loading all the cached objects from the
     * persisted stores. It does a total replacement of current cache
     * with the objects loaded from persistent stores and does not
     * perform a merge of any sort.
     *
     * For example, call this method after a cold start of your application
     * to reload the cache with persisted objects from last session.
     */
    fun rehydrate()
}