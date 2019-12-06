package com.suparnatural.core.graphql

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * A serializable GraphQl error as per the spec
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
 * A serializable GraphQl response as per the spec
 */
@Serializable
data class GraphQlResponse<T>(
        val data: T? = null,
        val errors: List<GraphQlResponseError>? = null,
        val extensions: JsonObject? = null
)

/**
 * A serializable GraphQl request as per the spec
 */
@Serializable
data class GraphQlRequest(
        val operationName: String,
        val query: String,
        val variables: Map<String, @ContextualSerialization Any?>
)

/**
 * A GraphQl operation encapsulates the query / mutation
 * and its corresponding response definition.
 */
abstract class GraphQlOperation<T> {
    /**
     * complete GraphQl query serialized as a string
     */
    abstract val source: String

    /**
     * name of the operation. It is sent as `operationName`
     * in the request.
     */
    abstract val name: String

    /**
     * A map of operation variables
     */
    abstract val variables: MutableMap<String, Any?>

    /**
     * A mutable map which holds context of the operation
     * which can be used by links down the chain
     */
    val context: MutableMap<String, Any> = mutableMapOf()

    /**
     * A serialized json string of the GraphQl operation
     */
    @UnstableDefault
    val jsonString: String
        get() {
            return Json.stringify(GraphQlRequest.serializer(), GraphQlRequest(name, source, variables))
        }

    /**
     * Serializer to parse the response body
     */
    abstract val responseSerializer: KSerializer<T>
}