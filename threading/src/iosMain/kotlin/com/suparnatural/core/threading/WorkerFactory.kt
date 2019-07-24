package com.suparnatural.core.threading

actual class WorkerFactory {
    actual companion object {
        actual val current: Worker
            get() {
                val currentNativeWorker = kotlin.native.concurrent.Worker.current ?: return main
                return BackgroundWorker(currentNativeWorker)
            }
        actual val main: Worker = MainWorker()

        actual fun newBackgroundWorker(): Worker {
            return BackgroundWorker()
        }
    }
}
