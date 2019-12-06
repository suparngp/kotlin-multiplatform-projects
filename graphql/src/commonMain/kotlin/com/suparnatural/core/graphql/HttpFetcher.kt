package com.suparnatural.core.graphql

/**
 * A request for HTTP fetcher.
 */

interface HttpFetchRequest<T> {

    /**
     * body of the http request.
     */
    val body: T?

    /**
     * Http request headers
     */
    val headers: Map<String, String>?
}

/**
 * A response for Http request
 */
interface HttpFetchResponse<T> {

    /**
     * Http response body
     */
    val body: T?

    /**
     * Http status code
     */
    val statusCode: Int

    /**
     * Http status message. It can be used to store failure messages
     */
    val statusMessage: String?
}

val <T> HttpFetchResponse<T>.isFailure: Boolean
    get() = this.statusCode != 200

/**
 * An HttpFetcher fetches an [HttpFetchResponse] for a given [HttpFetchRequest]
 * from an http server.
 */
interface HttpFetcher<T : HttpFetchRequest<*>, V : HttpFetchResponse<*>> {

    /**
     * Fetches the response of type `V` from a given url with request body
     * of type `T`.
     *
     * @param url the endpoint
     * @param request the request object
     * @param handler the result handler
     */
    fun fetch(url: String, request: T, handler: (V) -> Unit)
}