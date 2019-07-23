package com.suparnatural.core.threading

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.test.Test
import kotlin.test.assertEquals

class FutureIosTests {
    @Test
    fun testNativeFuture() {
        val worker = Worker.start()
        val future = worker.execute(TransferMode.SAFE, {Unit}) {
            "Hello"
        }
        assertEquals("Hello", NativeFuture(future).await())

    }
}