package com.suparnatural.core.threading

/**
 * A [Future] defers the resolution of a value to the future.
 * The value can be consumed by calling [Future.consume] method.
 * The implementation decides whether [Future.consume] blocks
 * the calling thread or not.
 */
interface Future<T> {

    /**
     * Consume the future value in `code` as input.
     * This method may or may not block the calling thread
     * depending upon the implementation.
     *
     * ### Examples
     *
     * ```
     * val future: Future<T> = // some api which returns a Future<T>
     * val value: R = future.consume {it: T ->
     *    return R()
     * }
     * ```
     */
    fun <R>consume(code: (T) -> R): R
}
