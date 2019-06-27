package com.suparnatural.core.threading

/**
 * Dispatches a given job on a particular thread.
 *
 * ### Examples
 *
 * ```
 * JobDispatcher.dispatchOnMainThread("Hello") {it: String ->
 *     assertEquals("Hello", it) // runs on main thread
 * }
 *
 * JobDispatcher.dispatchOnBackgroundThread("Hello") {it: String ->
 *     assertEquals("Hello", it) // runs on background thread
 * }
 * ```
 */
expect object JobDispatcher {

    /**
     * Dispatches a non-capturing lambda `job` with `payload` as its input on
     * the main thread. The `payload` is converted to an immutable object and then
     * passed to the job as argument.
     */
    fun <T> dispatchOnMainThread(payload: T, job: (T) -> Unit)

    /**
     * Dispatches a non-capturing lambda `job` with `payload` as its input on
     * a background thread. The `payload` is converted to an immutable object and then
     * passed to the job as argument.
     *
     * On Android, it uses Executor service and on iOS, it uses
     * background queue.
     */
    fun <T> dispatchOnBackgroundThread(payload: T, job: (T) -> Unit)
}