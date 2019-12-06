package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.subject.publish.publishSubject
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

/**
 * A Json based Http request where body is a plain Json string.
 */
open class JsonHttpFetchRequest(override val body: String?, override val headers: Map<String, String>? = null) : HttpFetchRequest<String>

/**
 * A Json based Http response where response body is a plain Json string
 */
open class JsonHttpFetchResponse(
        override val body: String?,
        override val statusCode: Int,
        override val statusMessage: String? = null
) : HttpFetchResponse<String>


/**
 * An [HttpFetcher] which can handle a Json request and a Json response
 */
interface JsonHttpFetcher : HttpFetcher<JsonHttpFetchRequest, JsonHttpFetchResponse>

/**
 * A default implementation of a Json based GraphQl client which accepts
 * Json as request body and emits a parsed object from a Json response
 */
class JsonHttpGraphQlClient(chain: Link<GraphQlOperation<*>, *, JsonHttpFetchResponse>) : GraphQlClient<JsonHttpFetchResponse>(chain) {

    /**
     * Error codes
     */
    enum class Error(val message: String) {
        INVALID_RESPONSE("Invalid http response"),
        MALFORMED_BODY("Malformed response body. Unable to parse")
    }

    /**
     * Encapsulates a failed response where [response] may contain the
     * GraphQl response with [GraphQlResponse.errors] property set.
     * If the error is not a standard GraphQl error (e.g. network errors),
     * the [response] will be null.
     */
    data class FailureResponse<T>(val error: Error, val response: GraphQlResponse<T>?)

    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation<T>): Observable<Result<GraphQlResponse<T>, FailureResponse<T>>> {
        return chain.execute(operation).map {
            try {
                val body = it.body ?: "{}"
                val json = Json.parse(GraphQlResponse.serializer(operation.responseSerializer), body)
                if (it.isFailure) {
                    return@map Result.Failure(FailureResponse(Error.INVALID_RESPONSE, json))
                }

                Result.Success(json)
            } catch (e: Exception) {
                Result.Failure(FailureResponse(Error.MALFORMED_BODY, null), e)
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
        val subject = publishSubject<JsonHttpFetchResponse>()
        val headers = defaultHeaders?.toMutableMap() ?: emptyMap()
        if (operation.context.containsKey(contextKeyHeaders)) {
            val overrides = operation.context[contextKeyHeaders]
            if (overrides is Map<*, *>) {
                overrides.entries.forEach {
                    headers[it.key as String] = it.value as String
                }
            }
        }
        fetcher.fetch(url, JsonHttpFetchRequest(operation.jsonString, headers)) {
            subject.onNext(it)
            subject.onComplete()
        }
        return subject
    }

}