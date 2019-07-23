package com.suparnatural.core.threading

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.staticCFunction
import platform.darwin.dispatch_async_f
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import platform.posix.QOS_CLASS_USER_INTERACTIVE
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.attach

actual object JobDispatcher {
    actual fun <T> dispatchOnMainThread(payload: T, job: (T) -> Unit) {
        val detached = DetachedObjectGraph { ThreadTransferableJob.create(payload, job) }.asCPointer()

        dispatch_async_f(dispatch_get_main_queue(), detached, staticCFunction { it: COpaquePointer? ->
            initRuntimeIfNeeded()
            @Suppress("UNCHECKED_CAST")
            val attached = DetachedObjectGraph<Any>(it).attach() as ThreadTransferableJob<T, T>
            attached.job(attached.payload)
        })
    }

    @ExperimentalUnsignedTypes
    actual fun <T> dispatchOnBackgroundThread(payload: T, job: (T) -> Unit) {

        val detached = DetachedObjectGraph { ThreadTransferableJob.create(payload, job) }.asCPointer()
        dispatch_async_f(
                dispatch_get_global_queue(QOS_CLASS_USER_INTERACTIVE.convert(), 0.convert()),
                detached,
                staticCFunction { it: COpaquePointer? ->
                    initRuntimeIfNeeded()
                    @Suppress("UNCHECKED_CAST")
                    val attached = DetachedObjectGraph<Any>(it).attach() as ThreadTransferableJob<T, T>
                    attached.job(attached.payload)

                })
    }
}

