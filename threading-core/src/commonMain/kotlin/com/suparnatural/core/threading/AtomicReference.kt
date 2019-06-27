package com.suparnatural.core.threading

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
expect class AtomicReference<T>(value_: T) {
    /**
     * The wrapped value
     */
    var value: T

    /**
     * Compares the current value to `expected` and if it matches, replaces with `new` value.
     * If the result is successful, it returns true, otherwise false.
     * The get and update operations are performed atomically. The `new` value must be an immutable
     * object. Use [toImmutable] to make an immutable version of an object.
     */
    fun compareAndSet(expected: T, new: T): Boolean
}