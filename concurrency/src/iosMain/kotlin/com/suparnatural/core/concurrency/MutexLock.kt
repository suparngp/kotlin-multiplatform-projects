package com.suparnatural.core.concurrency

import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import platform.posix.*

actual class MutexLock {
    private val mutex = nativeHeap.alloc<pthread_mutex_t>()
    init {
        pthread_mutex_init(mutex.ptr, null)
    }
    actual fun lock() {
        pthread_mutex_lock(mutex.ptr)
    }

    actual fun tryLock(): Boolean {
        return pthread_mutex_trylock(mutex.ptr) == 0
    }

    actual fun unlock() {
        pthread_mutex_unlock(mutex.ptr)
    }

    actual fun destroy() {
        pthread_mutex_destroy(mutex.ptr)
        nativeHeap.free(mutex.rawPtr)
    }
}