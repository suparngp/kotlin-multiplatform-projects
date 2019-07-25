package com.suparnatural.core.concurrency

import kotlin.test.*

class LockTests {
    @Test
    fun testReadWriteLock() {
        val lock = ReadWriteLock()
        lock.acquireReadLock()
        lock.releaseReadLock()
        lock.acquireWriteLock()
        lock.releaseWriteLock()
        lock.destroy()
    }

    @Test
    fun testMutex() {
        val mutex = MutexLock()
        mutex.lock()
        assertFalse(mutex.tryLock())
        val future = WorkerFactory.newBackgroundWorker().execute(mutex) {
            assertFalse(mutex.tryLock())
        }
        future.await()
        mutex.unlock()
        mutex.destroy()
    }
}