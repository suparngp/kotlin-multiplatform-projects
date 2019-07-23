package com.suparnatural.core.cache

import com.suparnatural.core.fs.PathComponent
import com.suparnatural.core.threading.Future

/**
 * A [CacheStore] schedules its operations (controlled by [CacheStore.blocking] parameter)
 * on a [CacheStoreWorker]. Workers are expected to operate in a single mode (blocking/non-blocking).
 * Therefore, each worker can assume the mode in which it will be invoked in and should schedule
 * all of its tasks (with exceptions) in that mode forever.
 *
 * Workers should always operate with a non-capturing lambda meaning, all the state outside of
 * the invocation context of a worker should be considered invisible to a worker task.
 *
 * ### Examples
 *
 * #### Non capturing task
 * ```
 * class MyBackgroundWorker: CacheStoreWorker {
 *     fun persistObject(input..., task...) {
 *         myHandlerBasedScheduler.execute {
 *             task(input)
 *         }
 *     }
 *
 *
 * }
 * ```
 */
interface CacheStoreWorker {

    /**
     * Invokes a strictly non-capturing lambda [task] with the [input] as an argument to persist [T].
     * The [task] must not access any state other than its arguments including
     * any immutable of frozen objects to guarantee thread safety.
     */
    fun <T: Cacheable> persistObject(
            input: Triple<T, PathComponent, List<CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>>?>,
            task: (it: Triple<T, PathComponent, List<CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>>?>) -> Unit
    )

    /**
     * Invokes a strictly non-capturing lambda [task] with [input] to delete
     * an object located at [input]. The [task] must not access any state other than its arguments including
     * any immutable of frozen objects to guarantee thread safety.
     */
    fun unlinkObject(input: PathComponent, task: (it: PathComponent) -> Unit)

    /**
     * Close all resource handles and terminate the worker.
     */
    fun terminate(): Future<Unit>
}