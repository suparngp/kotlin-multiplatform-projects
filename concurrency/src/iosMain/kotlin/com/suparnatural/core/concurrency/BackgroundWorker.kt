package com.suparnatural.core.concurrency

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze

class BackgroundWorker(private val worker: Worker = Worker.start()) : com.suparnatural.core.concurrency.Worker {
    override val id: Long = worker.id.toLong()

    override fun <T, V> execute(jobInput: T, job: (T) -> V): Future<V> {
        val future = worker.execute(TransferMode.SAFE, { Pair(jobInput, job).freeze() }) {
            it.second(it.first)
        }
        return NativeFuture(future)
    }

    override fun terminate(finishPendingTasks: Boolean): Future<Unit> {
        return NativeFuture(worker.requestTermination(finishPendingTasks))
    }


    override fun <T, V, W> executeAndResume(
            jobInput: T,
            job: (T) -> V,
            resumingWorker: com.suparnatural.core.concurrency.Worker,
            awaitResumingJob: Boolean,
            resumingJob: (V) -> W
    ): Future<V> {
        val future = worker.execute(TransferMode.SAFE, {
            ResumableJob.create(jobInput, job, resumingWorker, awaitResumingJob, resumingJob)
        }) { it1 ->
            val result = it1.job(it1.jobInput)
            val resumingFuture = it1.resumingWorker.execute(Pair(result, it1.resumingJob).toImmutable()) { it2 ->
                it2.second(it2.first)
            }
            if (it1.awaitResumingJob) resumingFuture.await()
            result
        }
        return NativeFuture(future);
    }

}