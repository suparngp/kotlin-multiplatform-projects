package com.suparnatural.core.graphql

import kotlinx.serialization.KSerializer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

fun <T> Json.fromJsonOrNull(deserializer: KSerializer<T>?, jsonElement: JsonElement?): T? {
    if (jsonElement == null || deserializer == null) return null

    return try {
        this.fromJson(deserializer, jsonElement)
    } catch (e: MissingFieldException) {
        null
    }
}