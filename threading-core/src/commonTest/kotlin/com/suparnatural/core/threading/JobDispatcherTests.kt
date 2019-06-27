package com.suparnatural.core.threading

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JobDispatcherTests {
    @Test
    fun testDispatchOnMainThread() {
        JobDispatcher.dispatchOnMainThread(Unit) {
            assertTrue(isMainThread())
        }
    }

    @Test
    fun testDispatchOnBackgroundThread() {
        JobDispatcher.dispatchOnBackgroundThread(Unit) {
            assertFalse(isMainThread())
        }
    }
}