package com.suparnatural.core.graphql

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Represents a GraphQl response error.
 */
@Serializable
data class GraphQlResponseError(
        val message: String? = null,
        val path: List<String>? = null,
        val locations: List<Location>? = null,
        val extensions: JsonObject? = null) {
    @Serializable
    data class Location(val line: Int? = null, val column: Int? = null)
}

/**
 * Represents GraphQl response
 */
@Serializable
data class GraphQlResponse<T>(
        val data: T? = null,
        val errors: List<GraphQlResponseError>? = null,
        val extensions: JsonObject? = null
)

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