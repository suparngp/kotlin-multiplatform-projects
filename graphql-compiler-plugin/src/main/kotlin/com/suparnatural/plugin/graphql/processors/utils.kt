package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.Exception

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
fun typeName(input: String): String = snakeToPascal(strippedType(input)).capitalize()