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
expect class Worker() {

    /**
     * Executes a non capturing lambda `task` with `input` as its arguments.
     *
     * Returns a [Future] of type [V]
     */
    fun <T, V>execute(input: T, task: (T) -> V): Future<V>

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