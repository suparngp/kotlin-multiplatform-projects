package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.subject.behavior.behaviorSubject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Mapper
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.lang.Exception

interface Fetcher<T, V> {
    fun fetch(url: String, request: T, headers: Map<String, String>?, handler: (V) -> Unit)
}


interface GraphQlFetcher<T: GraphQlFetcher.Response>: Fetcher<GraphQlOperation, T> {
    interface Response {
        val body: String?
    }
}

class GraphQlClient(private val chain: Link<GraphQlOperation, *, GraphQlFetcher.Response>) {

    @UnstableDefault
    fun <T> execute(operation: GraphQlOperation, serializer: KSerializer<T>): Observable<GraphQlResponse<T>> {
        return chain.execute(operation).map {fetchResponse ->
            val body = fetchResponse.body

            if (body == null || body.isEmpty()) throw Exception()

            val json  = Json.parse(JsonObject.serializer(), body)
            val response = MutableGraphQlResponse<T>()
            val data = json.getObjectOrNull("data")
            val errors = json.getArrayOrNull("errors")

            if(data == null && errors == null) throw Exception()

            if(data != null) {
                response.data = Mapper.unmap(serializer, data.content)
            }

            if(errors != null) {
                response.errors = errors.jsonArray.map {
                    Mapper.unmap(GraphQlResponseError.serializer(), it.jsonObject.content)
                }
            }
            response
        }
    }
}
