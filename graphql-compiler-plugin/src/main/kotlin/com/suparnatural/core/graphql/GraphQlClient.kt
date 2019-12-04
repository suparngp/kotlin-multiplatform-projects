package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import kotlinx.serialization.Mapper
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

interface Fetcher<T, V> {
    fun fetch(url: String, body: T, headers: Map<String, String>?, handler: (V) -> Unit)
}


interface GraphQlFetcher<T : GraphQlFetcher.Response> : Fetcher<String, T> {
    interface Response {
        val body: String?
    }
}

interface GraphQlClient {
    fun <T> execute(operation: GraphQlOperation<T>): Observable<GraphQlResponse<T>>
}

class DefaultGraphQlClient(private val chain: Link<GraphQlOperation<*>, *, GraphQlFetcher.Response>) : GraphQlClient {

    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation<T>): Observable<GraphQlResponse<T>> {
        return chain.execute(operation).map { fetchResponse ->
            val body = fetchResponse.body

            if (body == null || body.isEmpty()) throw Exception()

            val json = Json.parse(JsonObject.serializer(), body)
            val data = json.getObjectOrNull("data")
            val errors = json.getArrayOrNull("errors")

            // set raw response in the context
            operation.context["rawResponse"] = json

            if (data == null && errors == null) throw Exception()

            val responseData = if (data != null) Mapper.unmap(operation.responseSerializer, data.content) else null

            val responseErrors = errors?.jsonArray?.map {
                Mapper.unmap(GraphQlResponseError.serializer(), it.jsonObject.content)
            }

            GraphQlResponse(responseData, responseErrors)
        }
    }
}
