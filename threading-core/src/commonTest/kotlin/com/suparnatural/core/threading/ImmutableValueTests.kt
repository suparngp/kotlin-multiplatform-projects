package com.suparnatural.core.threading

import kotlin.test.Test

class ImmutableValueTests {

    class FD {
        var x by ImmutableValue<FD?>(null)
    }

    @Test
    fun testFrozenDelegate() {
        val f = toImmutable(FD())

        (0..10).map { Worker() }.map {
            it.execute(f) { ff ->
                repeat(20000) {
                    ff.x = FD()
                }
            }
        }.forEach {
            it.consume { }
        }
    }
}
