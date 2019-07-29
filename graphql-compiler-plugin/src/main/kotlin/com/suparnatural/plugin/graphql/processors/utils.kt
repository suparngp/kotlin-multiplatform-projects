package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.Field

val KnownTypes = mutableMapOf(
        "Int" to INT,
        "Float" to FLOAT,
        "String" to STRING,
        "Boolean" to BOOLEAN,
        "ID" to TypeAliasSpec.builder("ID", String::class).build().type
)

fun propertyTypeName(inputType: String, knownTypes: Map<String, TypeName>, config: SuparnaturalGraphqlExtension?): TypeName {
    val stripped = strippedType(inputType)
    val typeName: TypeName = if (inputType.startsWith("[")) {
        val innerType = Regex("\\[(.+)]!?$")
                .matchEntire(inputType)!!
                .groups[1]!!
                .value
        List::class.asTypeName().parameterizedBy(propertyTypeName(innerType, knownTypes, config))
    } else {
        knownTypes[stripped] ?: ClassName(config?.packageName ?: "", stripped)
    }
    return typeName.copy(nullable = !inputType.endsWith("!"))
}

fun snakeToPascal(input: String): String {
    return input
            .trim()
            .split("_").joinToString("") { it.toLowerCase().capitalize() }
}

fun strippedType(input: String): String = input.replace(Regex("[\\[\\]?!]"), "")

fun addPropertiesMutating(fields: List<Field>, interfaceSpec: TypeSpec.Builder?, classSpec: TypeSpec.Builder, config: SuparnaturalGraphqlExtension? = null) {
    val constructorSpec = FunSpec.constructorBuilder()
    fields.forEach {
        val propertyType = propertyTypeName(it.type, KnownTypes, config)
        interfaceSpec?.addProperty(it.responseName, propertyType)
        constructorSpec.addParameter(it.responseName, propertyType)
        val classPropertySpec = PropertySpec
                .builder(it.responseName, propertyType)
                .initializer(it.responseName)
        if (interfaceSpec != null) {
            classPropertySpec.addModifiers(KModifier.OVERRIDE)
        }
        classSpec.addProperty(classPropertySpec.build())
    }
    classSpec.primaryConstructor(constructorSpec.build())
}