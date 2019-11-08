package com.suparnatural.core.graphql

import com.fasterxml.jackson.databind.ser.std.StringSerializer
import kotlinx.serialization.*
import kotlinx.serialization.internal.IntSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder


abstract class GraphQlOperation {

    abstract val query: String
    abstract val variables: Map<String, @ContextualSerialization Any>
    abstract val operationName: String


    fun serialize(): String {
        val operation = mapOf<String, Any>(
                "query" to query,
                "variables" to variables,
                "operationName" to operationName
        )

        return Json.stringify(operation)
    }
}

