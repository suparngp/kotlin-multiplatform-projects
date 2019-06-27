package com.suparnatural.core.threading

import kotlin.reflect.KProperty

/**
 * A property delegate to create thread safe immutable frozen properties backed by [AtomicReference].
 * The property can be declared with `by [ImmutableValue] (initialValue)` keyword.
 * All get and set operations are then performed atomically.
 * Note that the values are immutable once set.
 * So any updates to the internal properties of the value will cause a failure.
 *
 * This is really useful if you want an immutable object so that it can be shared
 * among different threads with some dynamic behavior (like lazy initialization)
 * ### Examples
 *
 * ```
 *
 * // Since top level objects are always immutable, they can be accessed from any thread.
 * object SharedObject{
 *
 *  val person: Person? by ImmutableValue(null)
 *
 *  fun initialize(p: Person) { // any thread can now atomically update the person property.
 *      person = p // will succeed
 *
 *      person.name = ""  // will cause error
 *  }
 * }
 *
 * ```
 */
class ImmutableValue<T>(initialValue: T) {

    /**
     * Internal [AtomicReference] instance backing the property.
     */
    private val delegateReference = AtomicReference(toImmutable(initialValue))

    /**
     * Get the immutable value instance.
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = delegateReference.value

    /**
     * Makes the new value Immutable and sets the value
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        delegateReference.value = toImmutable(value)
    }
}