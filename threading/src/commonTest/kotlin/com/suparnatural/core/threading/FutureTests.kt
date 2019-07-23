package com.suparnatural.core.threading

import kotlin.test.Test
import kotlin.test.assertEquals

class FutureTests {
    val RESULT = "result"
    @Test
    fun testValueFuture() {
        val future = ValueFuture(RESULT)
        assertEquals(RESULT, future.await())
    }

    @Test
    fun testJobFuture() {
        val future = JobFuture({
            assertEquals("input", it)
            RESULT
        }, "input")
        assertEquals(RESULT, future.await())
    }

}