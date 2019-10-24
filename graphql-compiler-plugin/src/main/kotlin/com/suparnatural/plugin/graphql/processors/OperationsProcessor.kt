package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.Operation

const val OperationsContainer = "Operations"

fun processOperations(operations: List<Operation>, config: SuparnaturalGraphqlExtension): FileSpec {
    val fileSpec = FileSpec.builder(config.packageName, "Operations")
    val container = TypeSpec.classBuilder(OperationsContainer)

    operations.forEach {
        val operationContainer = TypeSpec.classBuilder(it.typeName)
        processFieldGroup(it, operationContainer, config, false)
    }
    return fileSpec.addType(container.build()).build()
}