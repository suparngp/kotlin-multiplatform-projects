package com.suparnatural.core.concurrency

/**
 * Returns an immutable instance of an object.
 *
 * On Android, it simply echoes the input back because threads can have shared state.
 * On iOS, it calls .toImmutable method.
 *
 * ### Examples
 *
 * ```
 * val person = toImmutable(Person(name = "Bob"))
 *
 * person.name = "Jerry" // error
 *
 * ```
 *
 */
expect fun <T> T.toImmutable(): T

/**
 * Returns if the current thread is main thread.
 *
 * ### Examples
 *
 * ```
 *
 * JobDispatcher.dispatchOnMainThread(Unit) {
 *     assertTrue(isMainThread())
 * }
 *
 * JobDispatcher.dispatchOnBackgroundThread(Unit) {
 *     assertFalse(isMainThread())
 * }
 *
 * ```
 */
expect fun isMainThread(): Boolean
