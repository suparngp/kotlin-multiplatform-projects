package com.suparnatural.plugins.graphql.processors

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.suparnatural.plugins.graphql.GraphQlPluginExtension
import com.suparnatural.plugins.graphql.models.Field
import kotlinx.serialization.SerialName
import kotlin.reflect.KClass

val KnownTypes = mutableMapOf(
    "Int" to INT,
    "Float" to FLOAT,
    "String" to STRING,
    "Boolean" to BOOLEAN,
    "ID" to TypeAliasSpec.builder("ID", String::class).build().type
)

fun propertyTypeName(inputType: String, knownTypes: Map<String, TypeName>, config: GraphQlPluginExtension?): TypeName {
    val stripped = strippedType(inputType)
    val typeName: TypeName = if (inputType.startsWith("[")) {
        val innerType = Regex("\\[(.+)]!?$")
            .matchEntire(inputType)!!
            .groups[1]!!
            .value
        List::class.asTypeName().parameterizedBy(
            propertyTypeName(
                innerType,
                knownTypes,
                config
            )
        )
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

fun addPropertiesMutating(
    fields: List<Field>,
    interfaceSpec: TypeSpec.Builder?,
    classSpec: TypeSpec.Builder,
    config: GraphQlPluginExtension? = null
) {
    val constructorSpec = FunSpec.constructorBuilder()
    fields.forEach {
        val propertyType = propertyTypeName(
            it.type,
            KnownTypes,
            config
        )

        val serializable = isSerializable(classSpec)

        // if the parameter name is upper case and the container is serializable, we lowercase
        // the name and add @SerialName annotation
        val adjustedName = serializedName(it.responseName, serializable)

        interfaceSpec?.addProperty(adjustedName, propertyType)

        constructorSpec.addParameter(parameterSpec(it.responseName, propertyType, serializable).build())

        val classPropertySpec = PropertySpec
            .builder(adjustedName, propertyType)
            .initializer(adjustedName)
        if (interfaceSpec != null) {
            classPropertySpec.addModifiers(KModifier.OVERRIDE)
        }
        classSpec.addProperty(classPropertySpec.build())
    }
    classSpec.primaryConstructor(constructorSpec.build())
}

fun parameterSpec(name: String, typeName: TypeName, serializable: Boolean = false): ParameterSpec.Builder {
    // if the container is serializable, then all property names are de capitalized

    val adjustedName = serializedName(name, serializable)

    val spec = ParameterSpec.builder(adjustedName, typeName)
    if (serializable && adjustedName != name) {
        spec.addAnnotation(
            AnnotationSpec.builder(SerialName::class).addMember("\"$name\"")
                .build()
        )
    }

    return spec
}

fun parameterSpec(name: String, kClass: KClass<*>): ParameterSpec.Builder {
    return parameterSpec(name, kClass.asTypeName())
}

fun isSerializable(spec: TypeSpec.Builder): Boolean {
    return spec.annotationSpecs.any { it.className.simpleName == "Serializable" }
}

fun serializedName(name: String, serializable: Boolean = false) = if(serializable) name.decapitalize() else name