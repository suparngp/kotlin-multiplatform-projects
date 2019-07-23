package com.suparnatural.core.threading

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilitiesTests {
    @Test
    fun testIsMainThread() {
        JobDispatcher.dispatchOnMainThread(Unit) {
            assertTrue(isMainThread())
        }
        JobDispatcher.dispatchOnBackgroundThread(Unit) {
            assertFalse(isMainThread())
        }
    }
}