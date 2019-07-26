package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.full.createType
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

fun propertySpecType(type: String): TypeName {
    val input = type.trim()
    val typeName = when {
        input.startsWith('[') -> {
            val inn = Regex("\\[(.+)]!?$")
                    .matchEntire(input)!!
                    .groups[1]!!
                    .value
            List::class.asTypeName().parameterizedBy(propertySpecType(inn))
        }
        else -> ClassName("", input.replace(Regex("!$"), ""))
    }

    return typeName.copy(nullable = !input.endsWith("!"))
}

fun propertyType(type: String): String {
    val input = type.trim()
    val stripped = when {
        input.startsWith('[') -> {
            val inner = Regex("\\[(.+)]!?$")
                    .matchEntire(input)!!
                    .groups[1]!!
                    .value
            "List<${propertyType(inner)}>"
        }
        else -> input
    }
    return if (input.endsWith("!"))
        stripped.replace(Regex("!$"), "")
    else
        "$stripped?"
}


fun snakeToPascal(input: String): String {
    return input
            .trim()
            .split("_").joinToString("") { it.toLowerCase().capitalize() }
}

fun stripType(input: String): String = input.replace(Regex("[\\[\\]?!]"), "")
fun className(input: String): String = snakeToPascal(stripType(input)).capitalize()