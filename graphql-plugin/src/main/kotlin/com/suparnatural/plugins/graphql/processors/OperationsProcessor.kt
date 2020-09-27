package com.suparnatural.plugins.graphql.processors

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.suparnatural.plugins.graphql.GraphQlPluginExtension
import com.suparnatural.plugins.graphql.models.Operation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

const val OperationsContainer = "Operations"

fun processOperations(operations: List<Operation>, config: GraphQlPluginExtension): FileSpec {
    val fileSpec = FileSpec.builder(config.packageName, "Operations")
    val container = TypeSpec.classBuilder(OperationsContainer)

    operations.forEach {
        val originalTypeName = it.typeName
        val operationContainer = TypeSpec.classBuilder(originalTypeName)

        val constructorSpec = FunSpec.constructorBuilder()
        val variableMapList = mutableListOf<String>()
        it.variables.forEach { v ->
            val type = propertyTypeName(
                v.type,
                KnownTypes,
                config
            )
            constructorSpec.addParameter(v.name, type)
            operationContainer.addProperty(PropertySpec.builder(v.name, type).initializer(v.name).build())
            variableMapList.add("\"${v.name}\" to Json.decodeFromString(Json.encodeToString(${v.name}))")
        }
        operationContainer.primaryConstructor(constructorSpec.build())

        // root response type suffixed with Response
        it.typeName = "${originalTypeName}Response"
        processFieldGroup(it, operationContainer, config, false)

        val responseType = ClassName(config.packageName, "Operations.$originalTypeName.${it.typeName}")
        operationContainer.superclass(
            ClassName("com.suparnatural.core.graphql", "GraphQlOperation")
                .parameterizedBy(responseType)
        )

        operationContainer
            .addProperty(
                PropertySpec
                    .builder("name", String::class.asTypeName(), KModifier.OVERRIDE)
                    .initializer("\"$originalTypeName\"".trimIndent())
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("source", String::class.asTypeName(), KModifier.OVERRIDE)
                    .initializer("\"\"\"${it.sourceWithFragments.replace("$", "\${\"$\"}")}\"\"\"".trimIndent())
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder(
                        "variables",
                        ClassName("kotlin.collections", "MutableMap")
                            .parameterizedBy(
                                String::class.asTypeName(),
                                JsonElement::class.asTypeName().copy(true)
                            ),
                        KModifier.OVERRIDE
                    )
                    .initializer("mutableMapOf(${variableMapList.joinToString(", ")})")
                    .build()

            )
            .addProperty(
                PropertySpec.builder(
                    "responseSerializer",
                    KSerializer::class.asTypeName().parameterizedBy(responseType),
                    KModifier.OVERRIDE
                )
                    .initializer("$responseType.serializer()")
                    .build()
            )

        container.addType(operationContainer.build())
    }
    return fileSpec.addType(container.build()).build()
}