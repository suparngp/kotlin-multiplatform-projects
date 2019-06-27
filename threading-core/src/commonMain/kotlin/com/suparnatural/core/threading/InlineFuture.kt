package com.suparnatural.core.threading

/**
 * A naive future implementation to wrap a given `result`.
 * It executes on the calling thread synchronously.
 *
 * ### Examples
 *
 * ```
 *
 * val future: Future<String> = InlineFuture("hello")
 * future.consume {it: String ->
 *     assertEquals("Hello", it)
 * }
 *
 * ```
 */
class InlineFuture<T>(private val result: T): Future<T> {
    override fun <R>consume(code: (T) -> R): R {
        return code(result)
    }
}