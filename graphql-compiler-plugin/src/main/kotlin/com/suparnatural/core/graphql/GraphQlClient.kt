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

class DefaultGraphQlClient(private val chain: Link<GraphQlOperation<*>, *, GraphQlFetcher.Response>): GraphQlClient {

    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation<T>): Observable<GraphQlResponse<T>> {
        return chain.execute(operation).map { fetchResponse ->
            val body = fetchResponse.body

            if (body == null || body.isEmpty()) throw Exception()

            val json = Json.parse(JsonObject.serializer(), body)

            val response = object : GraphQlResponse<T> {
                override var data: T? = null
                override var errors: List<GraphQlResponseError>? = null
            }
            val data = json.getObjectOrNull("data")
            val errors = json.getArrayOrNull("errors")

            if (data == null && errors == null) throw Exception()

            if (data != null) {
                response.data = Mapper.unmap(operation.responseSerializer, data.content)
            }

            if (errors != null) {
                response.errors = errors.jsonArray.map {
                    Mapper.unmap(GraphQlResponseError.serializer(), it.jsonObject.content)
                }
            }
            response
        }
    }
}
