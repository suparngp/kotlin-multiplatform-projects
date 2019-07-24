package com.suparnatural.core.concurrency

/**
 * A [MutexLock] is a locking mechanism which allows only one thread
 * to gain access to a resource protected by an instance of [MutexLock].
 * If more than one thread tries to acquire `lock`, only the first thread
 * is successful while the other threads either wait or return depending on
 * whether [lock] or [tryLock] was invoked.
 *
 * ### Examples
 * ```
 * val mutex = MutexLock()
 * mutex.lock()
 * assertFalse(mutex.tryLock())
 * val future = WorkerFactory.newBackgroundWorker().execute(mutex) {
 *   assertFalse(mutex.tryLock())
 * }
 * future.await()
 * mutex.unlock()
 * mutex.destroy()
 * ```
 */
expect class MutexLock() {

    /**
     * Acquires a lock on the mutex.
     * If the lock is already acquired by another thread,
     * then the subsequent callers of this method are blocked
     * until the lock is released.
     */
    fun lock()

    /**
     * Tries to acquire a lock on the mutex.
     * If the lock is already acquired by another thread,
     * this method returns immediately with a `false` value.
     * Otherwise, if the lock is obtained, this method returns `true`.
     */
    fun tryLock(): Boolean

    /**
     * Releases an acquired lock on the mutex.
     * This method may throw a RuntimeException depending on the platform.
     */
    fun unlock()

    /**
     * Releases any memory resources. The instance should
     * be discarded after calling this method.
     */
    fun destroy()
}