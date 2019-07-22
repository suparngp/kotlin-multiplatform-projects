package com.suparnatural.core.threading

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

actual object JobDispatcher {
    private val backgroundExecutor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    actual fun <T> dispatchOnMainThread(payload: T, job: (T) -> Unit) {
        mainHandler.post {
            job(payload)
        }
    }

    actual fun <T> dispatchOnBackgroundThread(payload: T, job: (T) -> Unit) {
        backgroundExecutor.execute{
            job(payload)
        }
    }
}