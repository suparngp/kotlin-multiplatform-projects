package com.suparnatural.core.concurrency

import java.util.concurrent.atomic.AtomicReference

actual class AtomicReference<T> actual constructor(value_: T) : AtomicReference<T>(value_) {
    actual var value: T
        get() = this.get()
        set(newValue) = this.set(newValue)
}
