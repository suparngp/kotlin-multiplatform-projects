package com.suparnatural.core.cache

import com.suparnatural.core.cache.CacheManager.cache
import com.suparnatural.core.cache.CacheManager.initialize
import com.suparnatural.core.concurrency.Immutability

/**
 * A thread-safe object which manages the [cache] based on initialization parameters.
 *
 * Note: Make sure to call [initialize] before accessing [cache].
 */
object CacheManager {

    /**
     * Cache object managed by the [CacheManager]. Ensure that the cache is initialized
     * before calling any methods on this object.
     */
    var cache: Cache by Immutability(PlaceholderCache())


    /**
     * Initialize the manager with `cache`. [Cache.size] must be > 1
     *
     */
    fun initialize(cache: Cache) {
        if (cache.size < 1) throw Exception("Cache size must be > 1")
        this.cache = cache
    }
}