package com.suparnatural.core.graphql

/**
 * Encapsulates a successful result of type T or a Failure of type V with an arbitrary throwable.
 */
sealed class Result<T, V> {
    data class Success<T, V>(val value: T) : Result<T, V>()
    data class Failure<T, V>(val error: V, val cause: Throwable? = null) : Result<T, V>()
}