package com.suparnatural.core.threading

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze

actual class Worker actual constructor() {
    private val worker = Worker.start()
    actual fun <T, V> execute(input: T, task: (T) -> V): Future<V> {
        val future = worker.execute(TransferMode.SAFE, {Pair(input, task).freeze()}) {
            it.second(it.first)
        }
        return BlockingFuture(future)
    }

    actual fun terminate(finishPendingTasks: Boolean): Future<Unit> {
        return BlockingFuture(worker.requestTermination(finishPendingTasks))
    }
}