package com.suparnatural.core.graphql

import kotlinx.serialization.*
import kotlinx.serialization.json.*

interface QueryProvider {
    val query: String
    val name: String
}

@Serializable
data class GraphQlRequest(
        val operationName: String,
        val query: String,
        val variables: Map<String, @ContextualSerialization Any>
) {
    @UnstableDefault
    fun serialize() = Json.stringify(GraphQlRequest.serializer(), this)
}

@Serializable
data class GraphQlResponseError(val message: String, val path: List<String>, val locations: List<Location>) {
    @Serializable
    data class Location(val line: Int, val number: Int)
}

interface GraphQlResponse<T>{
    val data: T?
    val errors: List<GraphQlResponseError>?
}

data class MutableGraphQlResponse<T>(override var data: T? = null, override var errors: List<GraphQlResponseError>? = null) : GraphQlResponse<T>

open class GraphQlOperation(private val queryProvider: QueryProvider, private val variables: Map<String, Any>, val context: Map<String, Any> = emptyMap()) {
    var request: GraphQlRequest = GraphQlRequest(queryProvider.name, queryProvider.query, variables)


    @UnstableDefault
    fun <T> parseResponse(jsonBody: JsonBody, graphQlResponseSerializer: KSerializer<T>): GraphQlResponse<T> {
        val response = MutableGraphQlResponse<T>()
        val json = Json.parse(JsonObject.serializer(), jsonBody)
        val data = json.getObjectOrNull("data")
        val errors = json.getArrayOrNull("errors")

        if(data == null && errors == null) return response

        if(data != null) {
            response.data = Mapper.unmap(graphQlResponseSerializer, data.content)
        }

        if(errors != null) {
            response.errors = errors.jsonArray.map {
                Mapper.unmap(GraphQlResponseError.serializer(), it.jsonObject.content)
            }
        }
        return response
    }

    @UnstableDefault
    fun <T>parseResponse(httpFetcherResponse: GraphQlHttpFetcher.Response, graphQlResponseSerializer: KSerializer<T>): Pair<GraphQlHttpFetcher.Response, GraphQlResponse<T>> {
        val body = httpFetcherResponse.body
        if (httpFetcherResponse.httpStatusCode != 200 || body == null || body.isEmpty()) return Pair(httpFetcherResponse, MutableGraphQlResponse())
        return Pair(httpFetcherResponse, parseResponse(body, graphQlResponseSerializer))
    }
}