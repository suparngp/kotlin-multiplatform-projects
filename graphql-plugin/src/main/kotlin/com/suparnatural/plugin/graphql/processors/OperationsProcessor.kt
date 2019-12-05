package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.Operation

const val OperationsContainer = "Operations"

fun processOperations(operations: List<Operation>, config: SuparnaturalGraphqlExtension): FileSpec {
    val fileSpec = FileSpec.builder(config.packageName, "Operations")
    val container = TypeSpec.classBuilder(OperationsContainer)

    operations.forEach {
        val operationContainer = TypeSpec.classBuilder(it.typeName)

        operationContainer
                .addProperty(PropertySpec.builder("source", String::class.asTypeName()).initializer("\"\"\"${it.sourceWithFragments}\"\"\"".trimIndent()).build())
                .addProperty("variables", Map::class.asClassName().parameterizedBy(String::class.asTypeName(), Any::class.asTypeName()))

        val constructorSpec = FunSpec.constructorBuilder()
        val variableMapList = mutableListOf<String>()
        it.variables.forEach {v ->
            val type = propertyTypeName(v.type, KnownTypes, config)
            constructorSpec.addParameter(v.name, type)
            operationContainer.addProperty(PropertySpec.builder(v.name, type).initializer(v.name).build())
            variableMapList.add("\"${v.name}\" to ${v.name}")
        }
        operationContainer.addInitializerBlock(CodeBlock.of("""
            variables = mutableMapOf(${variableMapList.joinToString(", ")})
        """.trimIndent()))
        operationContainer.primaryConstructor(constructorSpec.build())
        it.typeName = "${it.typeName}Response"
        processFieldGroup(it, operationContainer, config, false)
        container.addType(operationContainer.build())
    }
    return fileSpec.addType(container.build()).build()
}