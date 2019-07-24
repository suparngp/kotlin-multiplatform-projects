package com.suparnatural.core.threading

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import kotlin.test.Test
import kotlin.test.assertEquals

class FutureAndroidTests {
    @Test
    fun testBlockingFuture() {
        val futureTask = FutureTask{
            "Hello"
        }
        Executors.newSingleThreadExecutor().execute(futureTask)
        assertEquals("Hello", NativeFuture(futureTask).await())
    }
}