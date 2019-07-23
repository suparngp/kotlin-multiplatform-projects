package com.suparnatural.core.threading

actual class NativeFuture<T>(private val future: java.util.concurrent.Future<T>) : Future<T> {
    override var isCompleted: Boolean = future.isDone

    override fun await(): T {
        return future.get()
    }
}