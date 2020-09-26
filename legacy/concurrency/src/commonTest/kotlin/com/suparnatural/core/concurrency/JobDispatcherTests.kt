package com.suparnatural.core.concurrency

import kotlin.test.Test
import kotlin.test.assertEquals
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
        JobDispatcher.dispatchOnNewBackgroundThread(Unit) {
            assertFalse(isMainThread())
        }
    }

    @Test
    fun testDispatchOnWorker() {
        val worker = WorkerFactory.newBackgroundWorker()
        JobDispatcher.dispatchOnWorker(worker, Pair(worker, "input")) {
            assertEquals("input", it.second)
            assertEquals(WorkerFactory.current.id, it.first.id)
            assertFalse(isMainThread())
        }
    }

    @Test
    fun testDispatchOnCurrentThread() {
        val worker = WorkerFactory.newBackgroundWorker()
        worker.execute(Pair(worker, "input")) {
            JobDispatcher.dispatchOnCurrentThread(it) {
                assertEquals(it.first.id, WorkerFactory.current.id)
                assertEquals("input", it.second)
                assertFalse(isMainThread())
            }
        }
    }
}