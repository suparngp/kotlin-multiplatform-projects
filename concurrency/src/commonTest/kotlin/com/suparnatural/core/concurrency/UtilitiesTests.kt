package com.suparnatural.core.concurrency

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilitiesTests {
    @Test
    fun testIsMainThread() {
        JobDispatcher.dispatchOnMainThread(Unit) {
            assertTrue(isMainThread())
        }
        JobDispatcher.dispatchOnNewBackgroundThread(Unit) {
            assertFalse(isMainThread())
        }
    }
}