package com.suparnatural.core.graphql

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

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
data class GraphQlResponse<T>(val data: T?, val errors: List<GraphQlResponseError>?)

// Serializable GraphQlRequest
@Serializable
data class GraphQlRequest(
        val operationName: String,
        val query: String,
        val variables: Map<String, @ContextualSerialization Any?>
)

/**
 * A GraphQl operation consists of a request and the corresponding response.
 */
abstract class GraphQlOperation<T> {
    abstract val source: String
    abstract val name: String
    abstract val variables: MutableMap<String, Any?>
    val context: MutableMap<String, Any> = mutableMapOf()

    @UnstableDefault
    val serializedString: String
        get() {
            return Json.stringify(GraphQlRequest.serializer(), GraphQlRequest(name, source, variables))
        }
    abstract val responseSerializer: KSerializer<T>
}