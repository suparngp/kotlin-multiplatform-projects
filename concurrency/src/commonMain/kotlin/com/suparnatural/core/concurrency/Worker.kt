package com.suparnatural.core.concurrency

/**
 * [Worker] presents a unified API across all platforms to interact with threads.
 * A [Worker] can execute a job in its event loop. If needed, it can also resume the
 * flow on a different worker instance. All threads including `main` are exposed
 * via this API. For example, to get a [Worker] backed by `main` thread, use
 * [WorkerFactory.main].
 *
 * A worker `job` must satisfy the following requirements:
 * 1. The `job` must be a non state capturing lambda which does not capture any outside state.
 * 2. Any input required by the `job` must be passed before hand in the [execute] or [executeAndResume] method
 * `jobInput` parameters.
 * 3. The `job` input arguments must be treated as immutable to guarantee thread safety.
 *
 * The basic idea behind worker is to bring the same level of abstraction to every platform as Native has
 * because native concurrency is the most restrictive one.
 *
 * On iOS, it uses the Kotlin/Native's `Worker` API.
 * On Android, it uses `Handler`.
 *
 * ### Examples
 *
 * #### Run job on background Worker
 * ```
 *   val worker = WorkerFactory.newBackgroundThread()
 *
 *   // calling execute schedules a task on worker
 *   val future = worker.execute("Hello") {it: String ->
 *     assertEquals("Hello", it)
 *     "World"
 *   }
 *   // wait for worker to complete, use await
 *   val result: String = future.await()
 *   assertEquals("World", result)
 * ```
 *
 * #### Resume job on a different Worker
 *
 * ```
 * val worker1 = WorkerFactory.newBackgroundWorker()
 * val worker2 = WorkerFactory.newBackgroundWorker()
 * val future = worker2.executeAndResume(INPUT, {
 *   assertEquals(INPUT, it)
 *   OUTPUT
 * }, worker1, true) {
 *   assertEquals(OUTPUT, it)
 *   it
 * }
 * assertEquals(OUTPUT, future.await())
 * ```
 *
 * #### Resume Job on main worker
 *
 * ```
 * val worker = WorkerFactory.newBackgroundWorker()
 * val future = worker.executeAndResume(INPUT, {
 *   assertEquals(INPUT, it)
 *   OUTPUT
 * }, awaitResumingJob = true) {
 *   // called on main thread asynchronously
 *   assertEquals(OUTPUT, it)
 *   it
 * }
 *
 * // do not call future.await because it will block main thread.
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