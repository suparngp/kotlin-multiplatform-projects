package com.suparnatural.core.concurrency

import java.util.concurrent.atomic.AtomicReference

actual class AtomicReference<T> actual constructor(value: T) : AtomicReference<T>(value) {
    actual var value: T
        get() = this.get()
        set(newValue) = this.set(newValue)
}
