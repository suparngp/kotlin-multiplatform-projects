package com.suparnatural.core.cache

import com.suparnatural.core.fs.PathComponent
import com.suparnatural.core.threading.Future
import com.suparnatural.core.threading.Worker

/**
 * A background worker for the cache store which does not block the calling thread.
 * The calling thread cannot wait for the result. Therefore, this worker is supposed
 * to be used in a fire and forget scenario. Otherwise, use [CacheStoreBlockingWorker].
 */
class CacheStoreNonBlockingWorker : CacheStoreWorker {

    /**
     * An instance of [Worker] from threading.
     */
    val worker = Worker()

    /**
     * Adds an object by scheduling a task on the [worker].
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