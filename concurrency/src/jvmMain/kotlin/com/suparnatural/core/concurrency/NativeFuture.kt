package com.suparnatural.core.concurrency

/**
 * A [NativeFuture] returns an instance of platform specific Future wrapped in a common API.
 * Calling [NativeFuture.await] will wait until the result is available.
 *
 * The platform specific implementations have their own constructors which wrap
 * the native Future API.
 */
actual class NativeFuture<T>(private val future: java.util.concurrent.Future<T>) : Future<T> {
    override var isCompleted: Boolean = future.isDone

    override fun await(): T {
        return future.get()
    }
}