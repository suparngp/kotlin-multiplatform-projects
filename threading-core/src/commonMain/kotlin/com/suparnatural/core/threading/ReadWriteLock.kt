package com.suparnatural.core.threading

/**
 * A ReadWrite lock allows multiple readers to read a value
 * and only one writer to update it. A read lock cannot be obtained until
 * write lock is released. A write lock cannot be obtained until current
 * readers don't release read lock.
 *
 * ### Examples
 *
 * ```
 * val lock = ReadWriteLock()
 *
 * // read from multiple threads simultaneously.
 * lock.acquireReadLock() // call from as many threads
 *
 * // perform read ....
 *
 * lock.releaseReadLock()
 *
 * // to protect writes
 * lock.acquireWriteLock() // only one thread will get lock, others will be blocked.
 *
 * // perform write ....
 *
 * lock.releaseWriteLock() // next thread will now unblock.
 *
 * ```
 */
expect class ReadWriteLock() {

    /**
     * Destroys the lock and frees up the memory.
     * After calling this method, the instance can no longer be used.
     */
    fun destroy()

    /**
     * Acquire a read lock. This method will not return until the
     * lock is acquired.
     */
    fun acquireReadLock()

    /**
     * Releases the read lock
     */
    fun releaseReadLock()

    /**
     * Acquires the write lock. This method waits until all readers have
     * released the read lock. It then acquires the write lock and returns.
     */
    fun acquireWriteLock()

    /**
     * Releases the write lock
     */
    fun releaseWriteLock()
}