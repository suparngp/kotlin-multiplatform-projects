package com.suparnatural.core.graphql

/**
 * A Fetcher sends a request of type `T` to a server
 * and fetches a response of type `V`
 */
interface Fetcher<T, V> {
    fun fetch(request: T, handler: (V) -> Unit)
}

/**
 * A request for HTTP fetcher.
 */

abstract class HttpFetchRequest<T>(
        /**
         * Url to send request to.
         */
        val url: String,

        /**
         * body of the http request.
         */
        val body: T?,

        /**
         * Http request headers
         */
        val headers: Map<String, String>?
)

/**
 * A response for Http request
 */
abstract class HttpFetchResponse<T>(
        /**
         * Http response body
         */
        val body: T?,

        /**
         * Http status code
         */
        val statusCode: Int,

        /**
         * Http status message. It can be used to store failure messages
         */
        val statusMessage: String?
) {
    val isFailure: Boolean
        get() = this.statusCode != 200

}

/**
 * An HttpFetcher fetches an [HttpFetchResponse] for a given [HttpFetchRequest]
 * from an http server.
 */
interface HttpFetcher<T : HttpFetchRequest<*>, V : HttpFetchResponse<*>> : Fetcher<T, V>