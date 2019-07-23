package com.suparnatural.core.threading

import java.util.concurrent.locks.ReentrantReadWriteLock


actual class ReadWriteLock actual constructor() {
    private val lock = ReentrantReadWriteLock()

    actual fun destroy() { /* no destroy mechanism needed in android */
    }

    actual fun acquireReadLock() {
        lock.readLock().lock()
    }

    actual fun releaseReadLock() {
        lock.readLock().unlock()
    }

    actual fun acquireWriteLock() {
        lock.writeLock().lock()
    }

    actual fun releaseWriteLock() {
        lock.writeLock().unlock()
    }

}
