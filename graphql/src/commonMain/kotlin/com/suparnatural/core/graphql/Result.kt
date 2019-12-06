package com.suparnatural.core.graphql

/**
 * Encapsulates a successful result of type T or a Failure of type V with an arbitrary throwable.
 */
sealed class Result<T, V> {

    /**
     * Success result with a [value] of type `T`
     */
    data class Success<T, V>(
            /**
             * The result
             */
            val value: T
    ) : Result<T, V>()


    /**
     * Failure result with a [value] of type `V` and a throwable cause.
     * The [value] can hold details about the failure. For example, http status message.
     * The [cause] can be null if the failure was not caused by
     * an exception.
     */
    data class Failure<T, V>(
            /**
             * Info about the failure
             */
            val value: V,

            /**
             * A throwable cause of the failure
             */
            val cause: Throwable? = null
    ) : Result<T, V>()
}