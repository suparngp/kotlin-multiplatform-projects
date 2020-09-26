package com.suparnatural.core.utilities

import kotlin.test.Test
import kotlin.test.assertTrue

class UtilitiesCoreTests {

    private fun longRunningFunction() {
        var i = 0
        while (i < 10000000) {
            i += 1
        }
        return
    }
    @Test
    fun testMilliSeconds() {

        assertTrue {
            measureTimeMillis {
                longRunningFunction()
            } > 0
        }
    }

    @Test
    fun testMeasureMicroSeconds() {
        assertTrue {
            measureTimeMicros {
                longRunningFunction()
            } >= 0
        }
    }

    @Test
    fun testMeasureNanoSeconds() {
        assertTrue {
            measureNanoTime {
                longRunningFunction()
            } > 0
        }
    }
}
