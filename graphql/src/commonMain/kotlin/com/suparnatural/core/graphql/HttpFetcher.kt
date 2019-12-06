package com.suparnatural.core.graphql

// encapsulates request of an HttpFetcher
interface HttpFetchRequest<T> {
    val body: T?
}

// Encapsulates the response of an HttpFetcher
interface HttpFetchResponse<T> {
    val body: T?
    val statusCode: Int
    val statusMessage: String?
    val isFailure: Boolean
    val failureCause: String?
}

// Fetches a result of type V from a url by sending body type T
interface HttpFetcher<T : HttpFetchRequest<*>, V : HttpFetchResponse<*>> {
    fun fetch(url: String, body: T, headers: Map<String, String>?, handler: (V) -> Unit)
}

// Encapsulates an http fetch request with plain JSON body
open class JsonHttpFetchRequest(override val body: String?) : HttpFetchRequest<String>

// Encapsulates an http fetch response with plain JSON body
open class JsonHttpFetchResponse(
        override val body: String?,
        override val statusCode: Int,
        override val statusMessage: String? = null,
        override val isFailure: Boolean = false,
        override val failureCause: String? = null
) : HttpFetchResponse<String>

// Handles Http Requests with String request and response body
interface JsonHttpFetcher : HttpFetcher<JsonHttpFetchRequest, JsonHttpFetchResponse>