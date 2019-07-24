package com.suparnatural.core.threading

/**
 * A resumable job can be started in one worker and can be resumed in the original caller worker.
 * For example, an API call can be started in a background worker but its results can be delivered
 * on the main thread. The resumed job is a closure which takes the output of the original job as input.
 * The caller does not need to wait for the background worker to complete just like co-routines as the
 * resuming job is added to the event loop of the caller thread.
 *
 * **Note: This is an attempt to close the gap until multi-threaded coroutines become
 * available on both platforms.**
 *
 * @param JobInput the type of job input
 * @param ResultHandlerInput the type of job out put which will be passed to the result handler
 * @param ResultHandlerOutput the type of the
 * @property jobInput the input to the job which will execute on the target worker.
 * @property job the non capturing closure which will execute on the target worker.
 * @property resumingWorker the worker on which the job should be resumed once the target worker is finished.
 * @property awaitResumingJob whether target worker should wait for the resumed job closure to complete.
 * It is useful in testing.
 * @property resumingJob the non capturing closure which runs on the caller thread and accepts the `job` output
 * as the only argument.
 */
class ResumableJob<JobInput, ResultHandlerInput, ResultHandlerOutput>
private constructor(
        val jobInput: JobInput,
        val job: (JobInput) -> ResultHandlerInput,
        val resumingWorker: Worker,
        val awaitResumingJob: Boolean,
        val resumingJob: (ResultHandlerInput) -> ResultHandlerOutput
) {
    companion object {
        /**
         * Creates an immutable instance of [ResumableJob].
         *
         * @param JobInput the type of job input
         * @param ResultHandlerInput the type of job out put which will be passed to the result handler
         * @param ResultHandlerOutput the type of the
         * @property jobInput the input to the job which will execute on the target worker.
         * @property job the non capturing closure which will execute on the target worker.
         * @property resumingWorker the worker on which the job should be resumed once the target worker is finished.
         * @property awaitResumingJob whether target worker should wait for the resumed job closure to complete.
         * It is useful in testing.
         * @property resumingJob the non capturing closure which runs on the resumingWorker thread and accepts the `job` output
         * as the only argument.
         */
        fun <JobInput, ResultHandlerInput, ResultHandlerOutput> create(
                jobInput: JobInput,
                job: (JobInput) -> ResultHandlerInput,
                resumingWorker: Worker = WorkerFactory.current,
                awaitResumingJob: Boolean = false,
                resumingJob: (ResultHandlerInput) -> ResultHandlerOutput
        ): ResumableJob<JobInput, ResultHandlerInput, ResultHandlerOutput> {
            return (ResumableJob(jobInput, job, resumingWorker, awaitResumingJob, resumingJob).toImmutable())
        }
    }
}