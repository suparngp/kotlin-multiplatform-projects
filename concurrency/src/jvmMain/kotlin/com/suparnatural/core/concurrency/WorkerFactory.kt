package com.suparnatural.core.concurrency

import java.lang.Exception

/**
 * A factory which creates and returns [Worker] instances.
 */
actual class WorkerFactory {
    private class StubWorker(private val thread: Thread): Worker{
        override val id: Long
            get() = thread.id

        override fun <T, V> execute(jobInput: T, job: (T) -> V): Future<V> {
            throw Exception("Stub: Not implemented")
        }

        override fun <T, V, W> executeAndResume(jobInput: T, job: (T) -> V, resumingWorker: Worker, awaitResumingJob: Boolean, resumingJob: (V) -> W): Future<V> {
            throw Exception("Stub: Not implemented")
        }

        override fun terminate(finishPendingTasks: Boolean): Future<Unit> {
            throw Exception("Stub: Not implemented")
        }

    }
    actual companion object {
        /**
         * Returns the current worker which invoked this getter.
         */
        actual val current: Worker
            get() = StubWorker(Thread.currentThread())
        /**
         * Returns the worker backed by the main thread.
         */
        actual val main: Worker
            get() = newBackgroundWorker()

        /**
         * Creates and returns a new background worker.
         */
        actual fun newBackgroundWorker(): Worker {
            return ExecutorServiceWorker()
        }

    }
}