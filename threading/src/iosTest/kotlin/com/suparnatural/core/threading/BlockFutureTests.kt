package com.suparnatural.core.threading

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockFutureTests {
    @Test
    fun testBlockingFuture() {
        val worker = Worker.start()
        val future = worker.execute(TransferMode.SAFE, {Unit}) {
            "Hello"
        }
        BlockingFuture(future).consume {
            assertEquals("Hello", it)
        }

    }
}