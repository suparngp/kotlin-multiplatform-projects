package com.suparnatural.core.concurrency

import java.util.concurrent.atomic.AtomicReference


/**
 * An [AtomicReference] uses CAS instruction to get and update
 * the wrapped volatile variable to ensure lock free thread safety.
 *
 * ### Examples
 *
 * ```
 * val person = AtomicReference(toImmutable(Person()))
 * val newPerson = toImmutable(Person())
 * val value = person.value
 * person.compareAndSet(value, newPerson)
 * ```
 *
 *
 */
actual class AtomicReference<T> actual constructor(value_: T): AtomicReference<T>(value_) {
    /**
     * The wrapped value
     */
    actual var value: T
        get() = this.get()
        set(value) {
            this.set(value)
        }
}