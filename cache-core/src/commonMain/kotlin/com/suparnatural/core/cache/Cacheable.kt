package com.suparnatural.core.cache

/**
 * Any object which should be cached must be [Cacheable].
 * Each cache object must have a unique identifier returned by [cacheKey].
 * If the object wishes to be persistable, then the [serializeForPersistence]
 * method can return a string which represents its serialized version.
 *
 * ### Examples
 *
 * #### Make any object ready for caching.
 *
 * ```
 * class Person(val name: String, val id: String): Cacheable {
 *     // return the unique id as the cache key.
 *     fun cacheKey() = id
 *
 *     // persist a json string.
 *     fun serializeForPersistence() = Json.nonstrict.stringify(..., this)
 * }
 * ```
 */
interface Cacheable {
    /**
     * Returns the unique key used to identify the object in the cache.
     */
    fun cacheKey(): String

    /**
     * Returns a serialized string which can be persisted.
     * If this is the last object returned by the preprocessor chain,
     * the return value of this method will be persisted as it is.
     * Otherwise, this method will not called.
     */
    fun serializeForPersistence(): String

}