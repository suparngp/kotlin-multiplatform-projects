package com.suparnatural.core.threading

/**
 * A [Worker] executes tasks on a background thread.
 * A task must be a non capturing lambda depending only upon its
 * arguments for state. If a lambda captures any outside state,
 * the program will error out.
 *
 * On iOS, it uses the Kotlin/Native's Worker API.
 * On Android, it uses ExecutorService.
 *
 * ### Examples
 *
 * ```
 * val worker = Worker()
 * worker.execute("Hello") {it: String ->
 *     assertEquals("Hello", it) // executes on background thread.
 * }
 *
 * val name = "Bob"
 *
 * worker.execute("Jerry") {it: String ->
 *     println(name) // error because lambda captures outside state.
 * }
 * ```
 */
interface Worker {

    /**
     * Returns the identifier of this Worker.
     */
    val id: Long

    /**
     * Executes a non capturing lambda `job` with `jobInput` as its arguments.
     *
     * Returns a [Future] of type [V]
     */
    fun <T, V> execute(jobInput: T, job: (T) -> V): Future<V>

    /**
     * Executes a [job] the current instance with [jobInput] as input and then invokes the [resumingJob]
     * closure on the [resumingWorker] worker. The [resumingJob] closure accepts the output
     * of [job] as the only argument. Both [job] and [resumingJob] closures must be non capturing
     * lambdas. The job can be resumed on a different worker by passing [resumingWorker] argument
     * which is set to the current worker by default. The [awaitResumingJob] blocks the current instance
     * until [resumingJob] block is finished. This is useful in testing.
     *
     * For example
     * ```
     * // in main thread
     * worker.execute("input", {
     *
     *     // executes in worker 1
     *     "$it-processed"
     * }) {
     *     // executes on main thread
     *     println(it) // prints "$it-processed"
     * }
     * ```
     *
     * @param T the type of job input
     * @param V the type of job output which `resumingJob` closure takes as input.
     * @param W the type of `resumingJob` closure output.
     */
    fun <T, V, W> executeAndResume(
            jobInput: T,
            job: (T) -> V,
            resumingWorker: Worker = WorkerFactory.current,
            awaitResumingJob: Boolean = false,
            resumingJob: (V) -> W
    ): Future<V>

    /**
     * Terminates the worker. The pending tasks can be allowed
     * to finish by passing `finishPendingTasks` as `true`.
     * If false is passed, the worker is terminated immediately.
     *
     * Returns a future which resolves when the worker has been
     * terminated.
     *
     * Note that the future blocks the calling thread. So a rogue scheduled task
     * may hang the calling thread infinitely if `finishPendingTasks` is `true`.
     */
    fun terminate(finishPendingTasks: Boolean = true): Future<Unit>
}