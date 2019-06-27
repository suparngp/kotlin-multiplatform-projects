package com.suparnatural.core.threading

import kotlin.test.Test
import kotlin.test.assertEquals

class FutureTests {
    @Test
    fun testInlineFuture() {
        InlineFuture("Hello").consume {
            assertEquals(it, "Hello")
        }
    }
}