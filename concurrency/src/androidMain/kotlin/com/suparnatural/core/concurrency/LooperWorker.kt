package com.suparnatural.core.concurrency

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.util.concurrent.FutureTask

class LooperWorker(private val looper: Looper) : Worker {
    private val handler = Handler(looper)
    override val id: Long = looper.thread.id

    override fun <T, V> execute(jobInput: T, job: (T) -> V): Future<V> {
        val futureTask = FutureTask {
            job(jobInput)
        }
        handler.post(futureTask)
        return NativeFuture(futureTask)
    }

    override fun <T, V, W> executeAndResume(
            jobInput: T,
            job: (T) -> V,
            resumingWorker: Worker,
            awaitResumingJob: Boolean,
            resumingJob: (V) -> W
    ): Future<V> {
        val futureTask = FutureTask {
            val result = job(jobInput)
            val future = resumingWorker.execute(result, resumingJob)
            if (awaitResumingJob) future.await()
            result
        }
        handler.post(futureTask)
        return NativeFuture(futureTask)
    }

    override fun terminate(finishPendingTasks: Boolean): Future<Unit> {
        if (looper == Looper.getMainLooper()) {
            throw Exception("Cannot terminate main worker")
        }
        val thread = looper.thread
        if (thread is HandlerThread) {
            if (finishPendingTasks) thread.quitSafely() else thread.quit()
            return ValueFuture(Unit)
        } else {
            throw Exception("Looper thread is not HandlerThread")
        }
    }

}