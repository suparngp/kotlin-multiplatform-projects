package com.suparnatural.core.graphql

import com.fasterxml.jackson.databind.ser.std.StringSerializer
import kotlinx.serialization.*
import kotlinx.serialization.internal.IntSerializer
import kotlinx.serialization.json.*
import kotlin.jvm.Transient
import kotlin.reflect.KClass

interface QueryProvider {
    val query: String
    val name: String
}

open class GraphQlOperation(val queryProvider: QueryProvider, val variables: Map<String, Any>, initialContext: Map<String, Any>) {

    private val context = initialContext.toMutableMap()

    class Builder(from: GraphQlOperation) {
        private val operation = GraphQlOperation(from.queryProvider, from.variables, from.context.toMap())

        fun setContextValue(key: String, value: Any): Builder {
            operation.context[key] = value
            return this
        }
        fun build() = operation
    }

    @Serializable
    data class Request(
            val operationName: String,
            val query: String,
            val variables: Map<String, @ContextualSerialization Any>
    ) {
        @UnstableDefault
        fun serialize() = Json.stringify(Request.serializer(), this)
    }

    @Serializable
    data class Error(val message: String, val locations: List<Location>, val path: List<String>) {
        @Serializable
        data class Location(val line: Int, val number: Int)
    }

    fun dataSerializer() = JsonObject.serializer()

    class Response {
        val errors: List<Error>? = null
        val data: JsonElement? = null
    }

    data class ErrorLocation(val line: Int, val number: Int)
    data class ResponseError(val message: String, val locations: List<ErrorLocation>)

    fun buildResponse(json: JsonObject) {
    }


    fun request() = Request(queryProvider.name, queryProvider.query, variables)


}


@UnstableDefault
fun <T: GraphQlOperation> execute(operation: T) {
    val response: String = ""
    val json = Json.parse(JsonObject.serializer(), response)
    val data = json.getObjectOrNull("data")!!
    val errors = json.getArrayOrNull("errors")!!

    val name = operation.queryProvider.name
    val result = data[name]!!
    if (result is JsonArray) {
//        Mapper.unmap(operation.dataSerializer().list, result)
    } else if (result is JsonObject) {
        Mapper.unmap(operation.dataSerializer(), result)
    }

    val filter = errors.filter {
        it.jsonObject.getArray("path")[0].content == name
    }.map {
        Mapper.unmapNullable(GraphQlOperation.Error.serializer(), it.jsonObject)
    }



}