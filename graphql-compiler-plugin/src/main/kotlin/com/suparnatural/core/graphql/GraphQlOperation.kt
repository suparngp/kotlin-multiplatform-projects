package com.suparnatural.core.graphql

import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Provides query information about an operation
 */
interface QueryProvider {
    val query: String
    val name: String
}


/**
 * Represents a GraphQl response error.
 */
@Serializable
data class GraphQlResponseError(val message: String, val path: List<String>, val locations: List<Location>) {
    @Serializable
    data class Location(val line: Int, val number: Int)
}

/**
 * Represents GraphQl response
 */
interface GraphQlResponse<T>{
    val data: T?
    val errors: List<GraphQlResponseError>?
}

data class MutableGraphQlResponse<T>(override var data: T? = null, override var errors: List<GraphQlResponseError>? = null) : GraphQlResponse<T>

/**
 * A GraphQl operation consists of a request and the corresponding response.
 */
open class GraphQlOperation(private val queryProvider: QueryProvider, private val variables: Map<String, Any>, val context: Map<String, Any> = emptyMap()) {

    @UnstableDefault
    val serializedString: String
        get() {
            @Serializable
            data class GraphQlRequest(
                    val operationName: String,
                    val query: String,
                    val variables: Map<String, @ContextualSerialization Any>
            )
            return Json.stringify(GraphQlRequest.serializer(), GraphQlRequest(queryProvider.name, queryProvider.query, variables))
        }
}