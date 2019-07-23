package com.suparnatural.core.cache

/**
 * An internal placeholder no-op cache.
 */
internal class PlaceholderCache(override val persistentStores: List<CacheStore> = emptyList(), override val size: Int = 0) : Cache {
    override val replacementPolicy: CacheReplacementPolicy = FifoCacheReplacementPolicy(size)

    override fun getAllObjects(): List<Cacheable> = throw Error("Cache not initialized")

    override fun <T : Cacheable> getObject(key: String): T? = throw Error("Cache not initialized")

    override fun <T : Cacheable> addObject(obj: T): Boolean = throw Error("Cache not initialized")

    override fun <T : Cacheable> removeObject(key: String): Pair<T?, Boolean> = throw Error("Cache not initialized")

    override fun hashCode(key: String): Int = throw Error("Cache not initialized")

    override fun rehydrate() = throw Error("Cache not initialized")

}