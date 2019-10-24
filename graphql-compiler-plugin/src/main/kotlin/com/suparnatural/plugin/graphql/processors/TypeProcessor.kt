package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.TypeUsed
import com.suparnatural.plugin.graphql.models.TypeUsedKind
import kotlinx.serialization.Serializable

fun processTypes(types: List<TypeUsed>, config: SuparnaturalGraphqlExtension): FileSpec {
    val fileSpec = FileSpec
            .builder(config.packageName, "Types")
    // add all types to known types
    types.forEach {
        assert(!KnownTypes.containsKey(it.name)) { "${it.name} already exists in the KnownTypes. Check if it is a duplicate." }
        KnownTypes[it.name] = ClassName(config.packageName, it.name)
    }
    types.forEach { typeUsed ->

        val spec: TypeSpec.Builder

        when (typeUsed.kind) {
            TypeUsedKind.InputObjectType -> {
                spec = TypeSpec.classBuilder(typeUsed.name)
                val constructorSpec = FunSpec.constructorBuilder()
                typeUsed.fields.forEach { field ->
                    val type = propertyTypeName(field.type, KnownTypes, config)
                    constructorSpec.addParameter(field.name, type)
                    spec.addProperty(PropertySpec.builder(field.name, type).initializer(field.name).build())
                }
                spec.primaryConstructor(constructorSpec.build())
                        .addAnnotation(Serializable::class)
                        .addModifiers(KModifier.DATA)
            }
            TypeUsedKind.ScalarType -> {
                spec = TypeSpec.classBuilder(typeUsed.name)
                        .addAnnotation(AnnotationSpec.builder(Serializable::class)
                                .addMember("with = ${typeUsed.name}Serializer::class")
                                .build())
            }
            TypeUsedKind.EnumType -> {
                spec = TypeSpec.enumBuilder(typeUsed.name)
                typeUsed.values.forEach { enumValue ->
                    spec.addEnumConstant(enumValue.name)
                }
            }
        }
        fileSpec.addType(spec.build())
    }
    return fileSpec.build()
}