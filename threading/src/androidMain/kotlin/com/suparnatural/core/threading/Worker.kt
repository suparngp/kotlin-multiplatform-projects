package com.suparnatural.core.threading

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

class CallbackFuture<T>(private val task: () -> T): Future<T> {
    override fun <R> consume(code: (T) -> R): R {
        return code(task())
    }
}

actual class Worker actual constructor() {
    private val executor = Executors.newSingleThreadExecutor()

    actual fun <T, V> execute(input: T, task: (T) -> V): Future<V> {
        val futureTask: FutureTask<V> = FutureTask{
            task(input)
        }

        val androidTask = BlockingFuture(futureTask)
        executor.execute(futureTask)
        return androidTask
    }

    actual fun terminate(finishPendingTasks: Boolean): Future<Unit> {
        if(finishPendingTasks) {
            executor.shutdown()
        } else {
            executor.shutdownNow()
        }
        return CallbackFuture{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)
            return@CallbackFuture
        }
    }
}