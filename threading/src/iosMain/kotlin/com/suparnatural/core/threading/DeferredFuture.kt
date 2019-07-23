package com.suparnatural.core.threading

/**
 * A future whose value of type `T` can be set later.
 * Internally, it uses a mutex lock which is locked as soon as the instance
 * is created. Once the value is set, the lock is released.
 * Therefore, any callers of [await] are blocked until value is set.
 *
 * @param T the type of value this future instance holds.
 */
class DeferredFuture<T> : Future<T> {
    override var isCompleted: Boolean = false
    private val mutex = MutexLock()

    init {
        mutex.lock()
    }

    private var value: T? by Immutability(null)


    /**
     * Sets the [value] to the [newValue] of the future and releases the lock
     */
    fun setValue(newValue: T) {
        value = newValue
        isCompleted = true
        mutex.unlock()
    }

    override fun await(): T {
        val r: T
        if (!isCompleted) {
            mutex.lock()
            r = this.value!!
            mutex.unlock()
        } else {
            r = this.value!!
        }
        return r
    }
}