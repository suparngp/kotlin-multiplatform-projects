package com.suparnatural.core.threading

/**
 * A [BlockingFuture] which blocks the current thread to consume result.
 * Calling [BlockingFuture.consume] will wait until the result is available.
 *
 * The platform specific implementations have their own constructors which wrap
 * the native Future API.
 */
expect class BlockingFuture<T>: Future<T>