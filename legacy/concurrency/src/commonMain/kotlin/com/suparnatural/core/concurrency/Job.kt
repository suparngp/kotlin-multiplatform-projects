package com.suparnatural.core.concurrency

/**
 * A data structure to pass a payload and a callback job safely between threads by freezing its contents.
 * The [payload] of type [Input] can be used by the consumer of this object for processing.
 * The result of the processing of type [Output] can then be posted on a non capturing lambda [job] as argument.
 *
 * ### Examples
 *
 * ```
 * val safeJob = ThreadTransferableJob.create("Hello") {it: Int ->
 *      assertEquals("Hello".hashCode(), it)
 * }
 *
 * // other thread which receives job as argument.
 *
 * safeJob.job(safeJob.payload.hashCode())
 *
 * ```
 */
class Job<Input, Output>
private constructor(
        /**
         * A payload to be consumed by thread to which job is being transferred to.
         */
        val payload: Input,

        /**
         * A job to be executed by the target thread
         */
        val job: (Output) -> Unit
) {
    companion object {

        /**
         * Create a new [Job]
         */
        fun <I, O> create(payload: I, job: (O) -> Unit): Job<I, O> {
            return Job(payload, job).toImmutable()
        }
    }
}

