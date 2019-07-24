package com.suparnatural.core.threading

/**
 * A naive future implementation to wrap a given `result`.
 * It executes on the calling thread synchronously.
 *
 * ### Examples
 *
 * ```
 *
 * val future: Future<String> = ValueFuture("hello")
 * val result: String = future.await()
 *
 * ```
 */
class ValueFuture<T>(private val result: T) : Future<T> {
    override var isCompleted: Boolean = true
    override fun await(): T {
        return result
    }
}