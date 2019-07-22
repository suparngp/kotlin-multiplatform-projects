package com.suparnatural.core.threading

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class WorkerTests {
    @Test
    fun testWorker() {

        val workers = (0..10).map { Worker() }

        val futures = workers.map { w ->
            w.execute("Hello") {
                assertFalse(isMainThread())
                it
            }
        }

        futures.forEach { f ->
            f.consume {
                assertEquals(it, "Hello")
            }
        }

        workers.forEach {
            it.terminate(true)
        }
    }
}