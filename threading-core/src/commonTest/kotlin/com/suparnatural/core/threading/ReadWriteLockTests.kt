package com.suparnatural.core.threading

import kotlin.test.Test

class ReadWriteLockTests {
    @Test
    fun testReadWriteLock() {
        val lock = ReadWriteLock()
        lock.acquireReadLock()
        lock.releaseReadLock()
        lock.acquireWriteLock()
        lock.releaseWriteLock()
        lock.destroy()
    }

}