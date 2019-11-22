package com.suparnatural.core.concurrency

import java.util.concurrent.Semaphore

actual class MutexLock {
    val lock = Semaphore(1)
    private var isDestroyed = false

    private fun ensureNotDestroyed() {
        if (isDestroyed) throw Exception("MutexLock already destroyed")
    }
    actual fun lock() {
        ensureNotDestroyed()
        lock.acquire()
    }

    actual fun tryLock(): Boolean {
        ensureNotDestroyed()
        return lock.tryAcquire()
    }

    actual fun unlock() {
        ensureNotDestroyed()
        lock.release()
    }

    actual fun destroy() { /* no destroy mechanism needed in android */
        isDestroyed = true
    }
}