package com.suparnatural.core.threading

import android.net.Uri
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockFutureTests {
    @Test
    fun testBlockingFuture() {
        val futureTask = FutureTask{
            "Hello"
        }
        Executors.newSingleThreadExecutor().execute(futureTask)
        BlockingFuture(futureTask).consume {
            assertEquals("Hello", it)
        }
    }
}