package com.suparnatural.core.concurrency

import java.lang.Exception
import java.util.concurrent.locks.ReentrantReadWriteLock

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
actual class ReadWriteLock actual constructor() {
    private val lock = ReentrantReadWriteLock()

    private var isDestroyed = false

    private fun ensureNotDestroyed() {
        if (isDestroyed) throw Exception("ReadWriteLock already destroyed")
    }
    /**
     * Destroys the lock and frees up the memory.
     * After calling this method, the instance can no longer be used.
     */
    actual fun destroy() {
        isDestroyed = true
    }

    /**
     * Acquire a read lock. This method will not return until the
     * lock is acquired.
     */
    actual fun acquireReadLock() {
        ensureNotDestroyed()
        lock.readLock().lock()
    }

    /**
     * Releases the read lock
     */
    actual fun releaseReadLock() {
        ensureNotDestroyed()
        lock.readLock().unlock()
    }

    /**
     * Acquires the write lock. This method waits until all readers have
     * released the read lock. It then acquires the write lock and returns.
     */
    actual fun acquireWriteLock() {
        ensureNotDestroyed()
        lock.writeLock().lock()
    }

    /**
     * Releases the write lock
     */
    actual fun releaseWriteLock() {
        ensureNotDestroyed()
        lock.writeLock().unlock()
    }

}