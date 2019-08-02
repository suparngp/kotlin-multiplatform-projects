package com.suparnatural.core.graphql

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.*

@UnstableDefault
@Serializable(with = GraphQLOperationResultSerializer::class)
data class GraphQLOperationResult<D : Any, E : Any>(val data: D?, val extensions: E?, val errors: List<GraphQLError>?)

@UnstableDefault
@Serializer(forClass = GraphQLOperationResult::class)
class GraphQLOperationResultSerializer<D : Any, E : Any>(
        private val dataSerializer: KSerializer<D>,
        private val dataExtensionSerializer: KSerializer<E>?
) :
        KSerializer<GraphQLOperationResult<D, E>> {

    override val descriptor: SerialDescriptor = StringDescriptor.withName("OperationResultSerializer")

    override fun deserialize(decoder: Decoder): GraphQLOperationResult<D, E> {
        val json = (decoder as JsonInput).decodeJson().jsonObject
        val data = Json.nonstrict.fromJsonOrNull(dataSerializer, json["data"])
        val dataExtensions = Json.nonstrict.fromJsonOrNull(dataExtensionSerializer, json["extensions"])
        val errors = Json.nonstrict.fromJsonOrNull(GraphQLError.serializer().list, json["errors"])
        return GraphQLOperationResult(data, dataExtensions, errors)
    }

    override fun serialize(encoder: Encoder, obj: GraphQLOperationResult<D, E>) {
        val jsonOutput = encoder as JsonOutput
        val jsonMap = mutableMapOf<String, JsonElement>()
        if (obj.data != null) {
            jsonMap["data"] = Json.nonstrict.toJson(dataSerializer, obj.data)
        }
        val jsonObject = JsonObject(jsonMap)
        jsonOutput.encodeJson(jsonObject)
    }
}