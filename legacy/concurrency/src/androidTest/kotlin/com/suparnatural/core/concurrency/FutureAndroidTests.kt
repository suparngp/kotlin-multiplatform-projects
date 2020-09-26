package com.suparnatural.core.concurrency

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import kotlin.test.Test
import kotlin.test.assertEquals

class FutureAndroidTests {
    @Test
    fun testNativeFuture() {
        val futureTask = FutureTask{
            "Hello"
        }
        Executors.newSingleThreadExecutor().execute(futureTask)
        assertEquals("Hello", NativeFuture(futureTask).await())
    }
}