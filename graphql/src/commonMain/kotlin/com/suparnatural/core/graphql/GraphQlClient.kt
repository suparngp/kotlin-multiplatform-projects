package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json


interface GraphQlClient {
    fun <T> execute(operation: GraphQlOperation<T>): Observable<Result<GraphQlResponse<T>, *>>
}

class JsonHttpGraphQlClient(private val chain: Link<GraphQlOperation<*>, *, JsonHttpFetchResponse>) : GraphQlClient {

    enum class Error(val message: String) {
        INVALID_RESPONSE("Invalid http response"),
        MALFORMED_BODY("Malformed response body. Unable to parse")
    }

    data class FailureResponse<T>(val error: Error, val response: T)

    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation<T>): Observable<Result<GraphQlResponse<T>, FailureResponse<GraphQlResponse<T>?>>> {
        return chain.execute(operation).map {
            try {
                val body = it.body ?: "{}"
                val json = Json.parse(GraphQlResponse.serializer(operation.responseSerializer), body)
                if (it.isFailure) {
                    return@map Result.Failure<GraphQlResponse<T>, FailureResponse<GraphQlResponse<T>?>>(FailureResponse(Error.INVALID_RESPONSE, json))
                }

                Result.Success<GraphQlResponse<T>, FailureResponse<GraphQlResponse<T>?>>(json)
            } catch (e: Exception) {
                Result.Failure<GraphQlResponse<T>, FailureResponse<GraphQlResponse<T>?>>(FailureResponse(Error.MALFORMED_BODY, null), e)
            }
        }
    }
}
