package com.suparnatural.core.threading

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WorkerFactoryTests {
    @Test
    fun testMain() {
        assertNotNull(WorkerFactory.main)
    }

    @Test
    fun testNewBackgroundWorker() {
        val worker = WorkerFactory.newBackgroundWorker()
        assertNotNull(worker)
        val future = worker.execute(worker) {
            assertEquals(WorkerFactory.current.id, it.id)
        }
        future.await()
    }
}