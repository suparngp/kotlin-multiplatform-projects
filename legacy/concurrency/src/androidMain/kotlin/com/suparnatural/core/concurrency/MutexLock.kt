package com.suparnatural.core.concurrency

import java.util.concurrent.Semaphore

actual class MutexLock {
    val lock = Semaphore(1)
    private var isDestroyed = false

    private fun precheck() {
        if (isDestroyed) throw Exception("MutexLock already destroyed")
    }
    actual fun lock() {
        precheck()
        lock.acquire()
    }

    actual fun tryLock(): Boolean {
        precheck()
        return lock.tryAcquire()
    }

    actual fun unlock() {
        precheck()
        lock.release()
    }

    actual fun destroy() { /* no destroy mechanism needed in android */
        isDestroyed = true
    }
}