package com.suparnatural.core.concurrency

import android.os.HandlerThread
import android.os.Looper
import java.lang.Exception

actual class WorkerFactory {
    actual companion object {
        actual val current: Worker
            get() {
                val looper = Looper.myLooper()
                return if (looper != null) LooperWorker(looper) else throw Exception("Current thread not associated with any looper. ${Thread.currentThread().name}")
            }
        
        actual val main: Worker = LooperWorker(Looper.getMainLooper())

        actual fun newBackgroundWorker(): Worker {
            val handlerThread = HandlerThread("")
            handlerThread.start()
            return LooperWorker(handlerThread.looper)
        }
    }
}