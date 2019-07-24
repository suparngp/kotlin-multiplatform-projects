package com.suparnatural.core.concurrency

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AtomicReferenceTests {
    @Test
    fun testAtomicInt() {
        val number = AtomicReference(0)
        assertTrue (number.compareAndSet(0, 10))
        assertEquals (10, number.value)
        assertFalse(number.compareAndSet(0, 20))
        assertEquals(10, number.value)
    }

    @Test
    fun testAtomicBoolean() {
        val bool = AtomicReference(false)
        assertTrue (bool.compareAndSet(false, true))
        assertTrue (bool.value)
        assertFalse(bool.compareAndSet(false, true))
        assertTrue(bool.value)
    }

    @Test
    fun testPureAtomicReference() {
        class Person(val value: String = "")

        val ref = Person("name1").toImmutable()
        val person = AtomicReference(ref)
        assertFalse(person.compareAndSet(Person("name1"), Person("name2").toImmutable()))
        assertEquals(ref, person.value)

    }
}