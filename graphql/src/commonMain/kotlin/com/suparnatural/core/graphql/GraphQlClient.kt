package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json


interface GraphQlClient {
    fun <T> execute(operation: GraphQlOperation<T>): Observable<Result<GraphQlResponse<T>, *>>
}

enum class JsonHttpGraphQlClientError(val message: String) {
    INVALID_RESPONSE("Invalid http response"),
    MALFORMED_BODY("Malformed response body. Unable to parse")
}


class JsonHttpGraphQlClient(private val chain: Link<GraphQlOperation<*>, *, JsonHttpFetchResponse>) : GraphQlClient {
    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation<T>): Observable<Result<GraphQlResponse<T>, JsonHttpGraphQlClientError>> {
        return chain.execute(operation).map {
            try {
                if (it.isFailure) {
                    return@map Result.Failure<GraphQlResponse<T>, JsonHttpGraphQlClientError>(JsonHttpGraphQlClientError.INVALID_RESPONSE)
                }
                val body = it.body!!
                val json = Json.parse(GraphQlResponse.serializer(operation.responseSerializer), body)
                Result.Success<GraphQlResponse<T>, JsonHttpGraphQlClientError>(json)
            } catch (e: Exception) {
                Result.Failure<GraphQlResponse<T>, JsonHttpGraphQlClientError>(JsonHttpGraphQlClientError.MALFORMED_BODY, e)
            }
        }
    }
}
