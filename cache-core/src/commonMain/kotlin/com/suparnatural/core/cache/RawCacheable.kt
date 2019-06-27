package com.suparnatural.core.cache

/**
 * A container to hold raw version for a cached object read from a persistent store.
 * For example, in case of a disk store, [key] is the file name and [value]
 * is the file contents. This is the first Cacheable object passed to the
 * preprocessor chain. Therefore, your first preprocessor should
 * expect [RawCacheable] as the input to [CacheStorePreprocessor.unarchive] method.
 */
data class RawCacheable(val key: String, val value: String): Cacheable {
    override fun cacheKey(): String {
        return key
    }

    override fun serializeForPersistence(): String {
        return value
    }
}