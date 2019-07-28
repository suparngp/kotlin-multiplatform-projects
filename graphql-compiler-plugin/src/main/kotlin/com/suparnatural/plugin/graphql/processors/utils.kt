package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

val KnownTypes = mutableMapOf(
        "Int" to INT,
        "Float" to FLOAT,
        "String" to STRING,
        "Boolean" to BOOLEAN,
        "ID" to TypeAliasSpec.builder("ID", String::class).build().type
)

fun propertyTypeName(inputType: String, knownTypes: Map<String, TypeName>): TypeName {
    val stripped = strippedType(inputType)
    val typeName: TypeName = if (inputType.startsWith("[")) {
        val innerType = Regex("\\[(.+)]!?$")
                .matchEntire(inputType)!!
                .groups[1]!!
                .value
        List::class.asTypeName().parameterizedBy(propertyTypeName(innerType, knownTypes))
    } else {
        knownTypes[stripped] ?: throw Exception("Unknown type $inputType")
    }
    return typeName.copy(nullable = !inputType.endsWith("!"))
}

fun snakeToPascal(input: String): String {
    return input
            .trim()
            .split("_").joinToString("") { it.toLowerCase().capitalize() }
}

fun strippedType(input: String): String = input.replace(Regex("[\\[\\]?!]"), "")
