package com.suparnatural.core.concurrency

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JobDispatcherIosTests {
    @Test
    fun testDispatchOnCurrentMainThread() {
        JobDispatcher.dispatchOnCurrentThread("input") {
            assertEquals("input", it)
            assertTrue(isMainThread())
        }
    }
}
