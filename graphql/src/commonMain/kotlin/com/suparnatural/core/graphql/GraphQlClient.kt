package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable

/**
 * A client executes operations against a GraphQl server.
 * It accepts a chain of [Link] which emits the final response of type `T`.
 *
 * The client should not concern itself with how to make a network request.
 * Rather, it should rely on the last [Link] of the [chain] as terminating
 * link which fetches the response from the server. [JsonHttpGraphQlLink] is
 * one such link. The client should then take the response as input
 * and transform it into the correct response type based on the operation.
 *
 * @see JsonHttpGraphQlClient
 */
abstract class GraphQlClient<T>(val chain: Link<GraphQlOperation<*>, *, T>) {

    /**
     * Execute a [GraphQlOperation] with response type `V` and return the result
     * as an observable. The implementation of this method should first process
     * the [operation] via [chain]. The last link in the [chain] will return an observable
     * of type `T` which can then be mapped into a [Result] instance.
     */
    abstract fun <V> execute(operation: GraphQlOperation<V>): Observable<Result<GraphQlResponse<V>, *>>
}