package com.suparnatural.core.threading

import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import platform.posix.*

actual class ReadWriteLock {
    private val lockPtr = nativeHeap.alloc<pthread_rwlock_t>()
    init {
        pthread_rwlock_init(lockPtr.ptr, null)
    }

    actual fun destroy() {
        pthread_rwlock_destroy(lockPtr.ptr)
        nativeHeap.free(lockPtr)
    }

    actual fun acquireReadLock() {
        pthread_rwlock_rdlock(lockPtr.ptr)
    }

    actual fun releaseReadLock() {
        pthread_rwlock_unlock(lockPtr.ptr)
    }

    actual fun acquireWriteLock() {
        pthread_rwlock_wrlock(lockPtr.ptr)
    }

    actual fun releaseWriteLock() {
        pthread_rwlock_unlock(lockPtr.ptr)
    }
}