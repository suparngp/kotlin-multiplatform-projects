package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.Field
import com.suparnatural.plugin.graphql.models.Fragment
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault

fun addFieldsAsProperties(fields: List<Field>, interfaceSpec: TypeSpec.Builder?, classSpec: TypeSpec.Builder) {
    fields.forEach {
        val stripped = strippedType(it.type)
        if (!KnownTypes.containsKey(stripped)) {
            KnownTypes[stripped] = ClassName("", stripped)
        }
    }
    val constructorSpec = FunSpec.constructorBuilder()
    fields.forEach { field ->
        val propertyType = propertyTypeName(field.type, KnownTypes)
        interfaceSpec?.addProperty(field.responseName, propertyType)
        constructorSpec.addParameter(field.responseName, propertyType)
        classSpec.addProperty(PropertySpec.builder(field.responseName, propertyType).initializer(field.responseName).build())

        // complex field
        if (field.fields.isNotEmpty()) {
            val container = interfaceSpec ?: classSpec
            val hasFragments = field.fragmentSpreads.isNotEmpty()
            val typeName = strippedType(field.type)
            val spec: TypeSpec.Builder
            if (!hasFragments) {
                spec = TypeSpec.classBuilder(typeName)
                        .addModifiers(KModifier.DATA)
                        .addAnnotation(Serializable::class)
                addFieldsAsProperties(field.fields, null, spec)
                container.addType(spec.build())
            } else {
                spec = TypeSpec.interfaceBuilder(typeName)
                val implSpec = TypeSpec.classBuilder("${typeName}Impl")
                        .addModifiers(KModifier.DATA)
                        .addAnnotation(Serializable::class)
                        .addSuperinterface(ClassName("", typeName))

                addFieldsAsProperties(field.fields, spec, implSpec)
                spec.addType(implSpec.build())
            }
            container.addType(spec.build())
        }
    }
    // set the constructor
    classSpec.primaryConstructor(constructorSpec.build())
}

fun processFragment(fragment: Fragment, config: SuparnaturalGraphqlExtension, container: TypeSpec.Builder) {
    val spec = TypeSpec.interfaceBuilder(fragment.fragmentName)
    val implSpec = TypeSpec.classBuilder("${fragment.fragmentName}Impl")
            .addModifiers(KModifier.DATA)
            .addAnnotation(Serializable::class)
            .addSuperinterface(ClassName(config.packageName, fragment.fragmentName))
    addFieldsAsProperties(fragment.fields, spec, implSpec)

    spec.addType(implSpec.build())
    container.addType(spec.build())


}

fun processFragments(fragments: List<Fragment>, config: SuparnaturalGraphqlExtension): FileSpec {
    val fileSpec = FileSpec.builder(config.packageName, "Fragments")

    val containerInterface = TypeSpec.interfaceBuilder("Fragments")

    fragments.forEach { fragment ->
        processFragment(fragment, config, containerInterface)
    }


    return fileSpec
            .addType(containerInterface.build())
            .build()
}