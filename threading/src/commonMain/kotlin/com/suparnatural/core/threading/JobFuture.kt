package com.suparnatural.core.threading

/**
 * Wraps a [job] closure in a future. The [job] is not invoked until [await]
 * is called. The [jobInput] is passed as the only argument to job.
 *
 * @param T the type of the job
 * @param V the output of the job
 *
 * @property job the job to be executed in future
 * @property jobInput the only argument passed to the job
 */
class JobFuture<T, V>(val job: (T) -> V, val jobInput: T) : Future<V> {
    override var isCompleted: Boolean = false

    override fun await(): V {
        val result = job(jobInput)
        isCompleted = true
        return result
    }
}