package com.suparnatural.core.threading

actual class BlockingFuture<T>(private val future: kotlin.native.concurrent.Future<T>) : Future<T> {
    override fun <R>consume(code: (T) -> R): R {
        return future.consume {
            code(it)
        }
    }
}