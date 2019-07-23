package com.suparnatural.core.threading

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.staticCFunction
import platform.darwin.dispatch_async_f
import platform.darwin.dispatch_get_main_queue
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.attach

class MainWorker : Worker {
    override val id: Long = -1
    override fun <T, V, W> executeAndResume(
            jobInput: T,
            job: (T) -> V,
            resumingWorker: Worker,
            awaitResumingJob: Boolean,
            resumingJob: (V) -> W
    ): Future<V> {
        val deferred = DeferredFuture<V>()
        val detached = DetachedObjectGraph { Pair(ResumableJob.create(jobInput, job, resumingWorker, awaitResumingJob, resumingJob), deferred).toImmutable() }.asCPointer()

        dispatch_async_f(dispatch_get_main_queue(), detached, staticCFunction { it: COpaquePointer? ->
            initRuntimeIfNeeded()
            val attached = DetachedObjectGraph<Pair<ResumableJob<T, V, W>, DeferredFuture<V>>>(it).attach()
            val result = attached.first.job(attached.first.jobInput)
            attached.second.setValue(result)

            val future = attached.first.resumingWorker.execute(Pair(result, attached.first.resumingJob)) {
                it.second(it.first)
            }

            if (attached.first.awaitResumingJob) future.await()
            Unit
        })
        return deferred
    }

    override fun <T, V> execute(jobInput: T, job: (T) -> V): Future<V> {
        val deferred = DeferredFuture<V>()
        val detached = DetachedObjectGraph { Triple(jobInput, job, deferred).toImmutable() }.asCPointer()

        dispatch_async_f(dispatch_get_main_queue(), detached, staticCFunction { it: COpaquePointer? ->
            initRuntimeIfNeeded()
            val attached = DetachedObjectGraph<Triple<T, (T) -> V, DeferredFuture<V>>>(it).attach()
            val result = attached.second(attached.first)
            attached.third.setValue(result)
        })
        return deferred
    }

    override fun terminate(finishPendingTasks: Boolean): Future<Unit> {
        throw Exception("Cannot terminate Main Worker.")
    }

}