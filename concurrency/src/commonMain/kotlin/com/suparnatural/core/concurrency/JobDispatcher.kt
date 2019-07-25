package com.suparnatural.core.concurrency

/**
 * Dispatches a given job on a particular thread.
 *
 * ### Examples
 *
 * ```
 * val future = JobDispatcher.dispatchOnMainThread("Hello") {it: String ->
 *     assertEquals("Hello", it) // runs on main thread
 * }
 * future.await()
 *
 * val future = JobDispatcher.dispatchOnBackgroundThread("Hello") {it: String ->
 *     assertEquals("Hello", it) // runs on background thread
 * }
 * future.await()
 * ```
 */
object JobDispatcher {

    /**
     * Dispatches [job] with [jobInput] on the [worker] instance
     *
     * @param T the input of the [job]
     * @param V the output of the [job]
     */
    fun <T, V> dispatchOnWorker(worker: Worker, jobInput: T, job: (T) -> V): Future<V> {
        return worker.execute(jobInput, job)
    }

    /**
     * Dispatches a non-capturing lambda [job] with [jobInput] as its input on
     * the main thread. The [jobInput] is converted to an immutable object and then
     * passed to the job as argument.
     * @param T the input of the [job]
     * @param V the output of the [job]
     */
    fun <T, V> dispatchOnMainThread(jobInput: T, job: (T) -> V): Future<V> {
        return dispatchOnWorker(WorkerFactory.main, jobInput, job)
    }

    /**
     * Dispatches a non-capturing lambda [job] with [jobInput] as its input on
     * a background thread. The [jobInput] is converted to an immutable object and then
     * passed to the job as argument.
     *
     * On Android, it uses Executor service and on iOS, it uses
     * background queue.
     *
     * @param T the input of the [job]
     * @param V the output of the [job]
     */
    fun <T, V> dispatchOnNewBackgroundThread(jobInput: T, job: (T) -> V): Future<V> {
        return dispatchOnWorker(WorkerFactory.newBackgroundWorker(), jobInput, job)
    }

    /**
     * Dispatches a non-capturing lambda [job] with [jobInput] as its input on
     * the current thread. The [jobInput] is converted to an immutable object and then
     * passed to the job as argument.
     *
     *
     * @param T the input of the [job]
     * @param V the output of the [job]
     */
    fun <T, V> dispatchOnCurrentThread(jobInput: T, job: (T) -> V): Future<V> {
        return dispatchOnWorker(WorkerFactory.current, jobInput, job)
    }

}