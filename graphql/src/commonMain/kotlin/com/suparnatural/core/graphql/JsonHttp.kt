package com.suparnatural.core.graphql

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

/**
 * A Json based Http request where body is a plain Json string.
 */
open class JsonHttpFetchRequest(
        url: String,
        body: String? = null,
        headers: Map<String, String>? = null
) : HttpFetchRequest<String>(url, body, headers)

/**
 * A Json based Http response where response body is a plain Json string
 */
open class JsonHttpFetchResponse(
        body: String?,
        statusCode: Int,
        statusMessage: String? = null
) : HttpFetchResponse<String>(body, statusCode, statusMessage)


/**
 * An [HttpFetcher] which can handle a Json request and a Json response. Implement this interface
 * if only JSON payloads are handled.
 */
interface JsonHttpFetcher : HttpFetcher<JsonHttpFetchRequest, JsonHttpFetchResponse>

/**
 * A default implementation of a Json based GraphQl client which accepts
 * Json as request body and emits a parsed object from a Json response
 */
class JsonHttpGraphQlClient(chain: Link<GraphQlOperation<*>, *, JsonHttpFetchResponse>) : GraphQlClient<JsonHttpFetchResponse>(chain) {

    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation<T>): Observable<Result<GraphQlResponse<T>, GraphQlClientFailureResponse<T>>> {
        return chain.execute(operation).map {
            try {
                val body = it.body ?: "{}"
                val json = Json.parse(GraphQlResponse.serializer(operation.responseSerializer), body)
                if (it.isFailure) {
                    return@map Result.Failure<GraphQlResponse<T>, GraphQlClientFailureResponse<T>>(GraphQlClientFailureResponse(GraphQlClientError.INVALID_RESPONSE, json))
                }

                Result.Success<GraphQlResponse<T>, GraphQlClientFailureResponse<T>>(json)
            } catch (e: Exception) {
                Result.Failure<GraphQlResponse<T>, GraphQlClientFailureResponse<T>>(GraphQlClientFailureResponse(GraphQlClientError.MALFORMED_BODY, null), e)
            }
        }
    }
}

/**
 * A default implementation of a [Link] which fetches response from a GraphQl server
 * in Json format by using a [JsonHttpFetcher] instance.
 */
open class JsonHttpGraphQlLink(

        /**
         * The fetcher used to perform network operation
         */
        val fetcher: JsonHttpFetcher,

        /**
         * Endpoint where requests are sent
         */
        val url: String,

        /**
         * Default set of headers included with every request
         */
        val defaultHeaders: Map<String, String>? = null
) : Link<GraphQlOperation<*>, Unit, JsonHttpFetchResponse> {

    private val contextKeyHeaders = "headers"

    @UnstableDefault
    override fun execute(
            operation: GraphQlOperation<*>,
            next: Link<GraphQlOperation<*>, *, Unit>?
    ): Observable<JsonHttpFetchResponse> {
        val subject = DefaultPublishSubjectFactory.create<JsonHttpFetchResponse>()
        val headers = defaultHeaders?.toMutableMap() ?: mutableMapOf()
        if (operation.context.containsKey(contextKeyHeaders)) {
            val overrides = operation.context[contextKeyHeaders]
            if (overrides is Map<*, *>) {
                overrides.entries.forEach {
                    headers[it.key as String] = it.value as String
                }
            }
        }
        fetcher.fetch(JsonHttpFetchRequest(url, operation.jsonString, headers)) {
            subject.onNext(it)
            subject.onComplete()
        }
        return subject.asObservable()
    }

}