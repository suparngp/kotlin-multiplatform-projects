package com.suparnatural.core.threading

import kotlin.test.Test

class ImmutableValueTests {

    class FD {
        var x by Immutability<FD?>(null)
    }

    @Test
    fun testFrozenDelegate() {
        val f = FD().toImmutable()

        (0..10).map { WorkerFactory.newBackgroundWorker() }.map {
            it.execute(f) { ff ->
                repeat(20000) {
                    ff.x = FD()
                }
            }
        }.forEach {
            it.await()
        }
    }
}
