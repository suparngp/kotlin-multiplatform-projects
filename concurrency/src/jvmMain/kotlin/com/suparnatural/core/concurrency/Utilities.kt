package com.suparnatural.core.concurrency

import java.lang.Exception

/**
 * Returns an immutable instance of an object.
 *
 * On Android, it simply echoes the input back because threads can have shared state.
 * On iOS, it calls .toImmutable method.
 *
 * ### Examples
 *
 * ```
 * val person = Person(name = "Bob").toImmutable(
 *
 * person.name = "Jerry" // error
 *
 * ```
 *
 */
actual fun <T> T.toImmutable(): T {
    return this
}

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
actual fun isMainThread(): Boolean {
    throw Exception("Not Supported on JVM")
}