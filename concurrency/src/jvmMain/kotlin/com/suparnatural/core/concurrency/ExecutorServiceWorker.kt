package com.suparnatural.core.concurrency

import java.util.concurrent.*


class SimpleThreadFactory : ThreadFactory {
    val id: Long
        get() = thread?.id ?: 0

    private var thread: Thread? = null

    override fun newThread(r: Runnable): Thread {
        thread = Thread(r)
        return thread!!
    }
}

class ExecutorServiceWorker : Worker {
    override val id: Long
        get() = threadFactory.id

    private val threadFactory = SimpleThreadFactory()


    private val executor = ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>(), threadFactory)

    override fun <T, V> execute(jobInput: T, job: (T) -> V): Future<V> {

        val task = Callable<V> {
            job(jobInput)
        }
        val future = executor.submit(task)
        return NativeFuture(future)
    }

    override fun <T, V, W> executeAndResume(jobInput: T, job: (T) -> V, resumingWorker: Worker, awaitResumingJob: Boolean, resumingJob: (V) -> W): Future<V> {
        val task = Callable {
            val result = job(jobInput)
            val future = resumingWorker.execute(result, resumingJob)
            if (awaitResumingJob) future.await()
            result
        }
        val future = executor.submit(task)
        return NativeFuture(future)

    }

    override fun terminate(finishPendingTasks: Boolean): Future<Unit> {
        if (finishPendingTasks) {
            executor.shutdown()
        } else {
            executor.shutdownNow()
        }
        return ValueFuture(Unit)
    }

}