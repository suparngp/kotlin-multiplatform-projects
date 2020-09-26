package com.suparnatural.core.cache

import com.suparnatural.core.concurrency.Immutability
import com.suparnatural.core.concurrency.toImmutable

/**
 * A Container which holds [Cacheable] objects which can identified by the [key] derived from [Cacheable.cacheKey].
 * The cached object is converted to immutable before caching. So any updates to the same object will cause an error.
 */
data class CacheableContainer(
        /**
         * [Cacheable] object to be cached.
         */
        val obj: Cacheable
) {
    /**
     * Cache Key for `obj`
     */
    val key = obj.cacheKey()

    /**
     * A pointer to next cached object. Useful in Cache Registry and book keeping.
     */
    var next: CacheableContainer? by Immutability(null)

    /**
     * A pointer to previous cached object. Useful in Cache Registry and book keeping.
     */
    var previous: CacheableContainer? by Immutability(null)

    init {
        this.toImmutable()
    }
}
