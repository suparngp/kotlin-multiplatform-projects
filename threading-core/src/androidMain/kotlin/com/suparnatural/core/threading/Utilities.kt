package com.suparnatural.core.threading

import android.os.Looper

actual fun <T> toImmutable(obj: T): T {
    return obj
}

actual fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}


