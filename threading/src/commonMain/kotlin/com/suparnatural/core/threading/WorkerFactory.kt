package com.suparnatural.core.threading

/**
 * A factory which creates and returns [Worker] instances.
 */
expect class WorkerFactory {
    companion object {

        /**
         * Returns the current worker which invoked this getter.
         */
        val current: Worker

        /**
         * Returns the worker backed by the main thread.
         */
        val main: Worker

        /**
         * Creates and returns a new background worker.
         */
        fun newBackgroundWorker(): Worker
    }
}