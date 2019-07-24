package com.suparnatural.core.threading

/**
 * A [NativeFuture] returns an instance of platform specific Future wrapped in a common API.
 * Calling [NativeFuture.await] will wait until the result is available.
 *
 * The platform specific implementations have their own constructors which wrap
 * the native Future API.
 */
expect class NativeFuture<T>: Future<T>