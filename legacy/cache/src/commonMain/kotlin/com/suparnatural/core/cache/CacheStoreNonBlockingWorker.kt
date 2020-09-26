package com.suparnatural.core.cache

import com.suparnatural.core.concurrency.Future
import com.suparnatural.core.concurrency.Worker
import com.suparnatural.core.concurrency.WorkerFactory
import com.suparnatural.core.fs.PathComponent

/**
 * A background worker for the cache store which does not block the calling thread.
 * The calling thread cannot wait for the result. Therefore, this worker is supposed
 * to be used in a fire and forget scenario. Otherwise, use [CacheStoreBlockingWorker].
 */
class CacheStoreNonBlockingWorker : CacheStoreWorker {

    /**
     * An instance of [com.suparnatural.core.concurrency.Worker].
     */
    val worker = WorkerFactory.newBackgroundWorker()

    /**
     * Adds an object by scheduling a task on the [worker].
     *
     * @param T the type of object being persisted.
     */
    override fun <T: Cacheable> persistObject(
            input: Triple<T, PathComponent, List<CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>>?>,
            task: (it: Triple<T, PathComponent, List<CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>>?>) -> Unit
    ) {
        worker.execute(input, task)
    }

    /**
     * Removes an object by scheduling a task on the [worker].
     */
    override fun unlinkObject(input: PathComponent, task: (it: PathComponent) -> Unit) {
        worker.execute(input, task)
    }

    /**
     * Terminates the [worker]. This method is always invoked on the calling thread.
     */
    override fun terminate(): Future<Unit> {
        return worker.terminate(true)
    }

}