package com.suparnatural.core.threading

import java.util.concurrent.FutureTask

actual class BlockingFuture<T>(private val futureTask: FutureTask<T>): Future<T> {
    override fun <R> consume(code: (T) -> R): R {
        return code(futureTask.get())
    }
}