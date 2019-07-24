package com.suparnatural.core.threading

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
