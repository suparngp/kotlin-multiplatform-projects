package com.suparnatural.core.concurrency

/**
 * A [Future] defers the resolution of a value to the future.
 * The value can be consumed by calling [Future.await] method.
 * The implementation decides whether [Future.await] blocks
 * the calling thread or not.
 */
interface Future<T> {

    /**
     * Returns `true` if the future has completed and is ready to [await].
     */
    var isCompleted: Boolean
    /**
     * Consume the future value in `code` as input.
     * This method may or may not block the calling thread
     * depending upon the implementation.
     *
     * ### Examples
     *
     * ```
     * val future: Future<T> = // some api which returns a Future<T>
     * val value: R = future.await() // blocks until future completes
     * ```
     */

    fun await(): T
}
