package com.suparnatural.core.cache

import com.suparnatural.core.threading.Future


/**
 * A persistent store to back a cache.
 * This store is considered to be slow by default.
 * Therefore, direct interaction with it to access cached objects
 * should be avoided. The store is useful in certain scenarios.
 *
 * For example, an in-memory cache can be rehydrated after a cold
 * start of the application by loading from the backing [CacheStore].
 */
interface CacheStore {

    /**
     * Chain of preprocessors to archive and unarchive [Cacheable] objects.
     * While persisting, the preprocessors are applied to the incoming [Cacheable] instance
     * and are expected to return an archived [Cacheable] instance.
     * Before archiving, a [CacheStore] always adds a special [CacheStorePreprocessor] at the end
     * of the [preprocessors] chain which outputs a [RawCacheable].
     *
     * While reading an object for the persistence, the preprocessors are applied in a reverse order.
     * The store first unarchives the read content into a [RawCacheable] and then passes to the
     * last (or first preprocessor in reversed [preprocessors] chain) preprocessor in the chain moving its way
     * back to the first (or the last in reverse [preprocessors] chain).
     */
    val preprocessors: List<CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>>?

    /**
     * Marker whether the store does any blocking I/O. If it is set to true, then
     * it is expected that the calling thread will be blocked. Otherwise, certain method calls
     * on the [CacheStore] will return immediately and all the operations will be scheduled on
     * a separate worker thread.
     *
     * Note: If blocking is set to false, then the store does not guarantee that the objects
     * will be persisted immediately. For example, if the application terminates, some tasks
     * scheduled on the worker may not finish and therefore, some objects may not be persisted.
     *
     * If guaranteed persistence is desired, this flag should be set to true.
     * For maximum performance, this can be set to false.
     */
    val blocking: Boolean

    /**
     * Persists a [Cacheable] `obj` to the store. Uses a background worker if [blocking] is false.
     */
    fun <T : Cacheable> persistObject(obj: T)

    /**
     * Deletes an object with the given `key`. Uses a background worker is [blocking] is false.
     */
    fun unlinkObject(key: String)

    /**
     * Close all the handles to the resources by flushing any scheduled tasks.
     * This method should be used in conjunction when [blocking] is set to true.
     * The store implementation is expected to guarantee that calling this method
     * will not result in any loss and that all resources will be freed only after
     * the schedules tasks are completed.
     *
     * For example, call this on [DiskStore] to close any file handles and terminate the worker.
     */
    fun flushAndClose(): Future<Unit>

    /**
     * Returns a list containing all the objects persisted in the store. This method is always
     * invoked on the calling thread and is not affected by [blocking] value.
     */
    fun fetchAllObjects(): List<Cacheable>

    /**
     * Calling this method guarantees that all traces of persisted objects are
     * removed from the system. This method is always invoked on the calling thread
     * and is not affected by [blocking] value.
     */
    fun wipe()
}
