package com.suparnatural.core.cache

import com.suparnatural.core.threading.ImmutableValue
import com.suparnatural.core.threading.toImmutable

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
    var next: CacheableContainer? by ImmutableValue(null)

    /**
     * A pointer to previous cached object. Useful in Cache Registry and book keeping.
     */
    var previous: CacheableContainer? by ImmutableValue(null)

    init {
        toImmutable(this)
    }
}
