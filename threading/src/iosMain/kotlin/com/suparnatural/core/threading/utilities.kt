package com.suparnatural.core.threading

import platform.Foundation.NSThread
import kotlin.native.concurrent.freeze

actual fun <T> toImmutable(obj: T): T {
    return obj.freeze()
}

actual fun isMainThread(): Boolean {
    return NSThread.isMainThread
}
