package com.suparnatural.core.threading

import kotlin.native.concurrent.FutureState

actual class NativeFuture<T>(private val future: kotlin.native.concurrent.Future<T>) : Future<T> {
    override var isCompleted: Boolean = future.state != FutureState.SCHEDULED

    override fun await(): T {
        return future.consume { it }
    }
}