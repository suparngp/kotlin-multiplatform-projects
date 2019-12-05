package com.suparnatural.plugins.graphql.processors

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.suparnatural.plugins.graphql.GraphQlPluginExtension
import com.suparnatural.plugins.graphql.models.FieldGroup
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.*

const val FragmentsGroup = "FragmentsGroup"
const val FragmentsAdapter = "FragmentsAdapter"
const val FragmentsContainer = "Fragments"
const val FragmentsAdapterDelegateProperty = "delegate"
const val FragmentsAdapterFragmentsProperty = "fragments"

fun processFieldGroup(
    fieldGroup: FieldGroup,
    container: TypeSpec.Builder,
    config: GraphQlPluginExtension,
    top: Boolean = false
) {

    val rootSpecTypeName = strippedType(fieldGroup.typeName)
    val rootClassName = ClassName("", rootSpecTypeName)

    val hasFragments = fieldGroup.fragmentSpreads.isNotEmpty()
    val rootSpec: TypeSpec.Builder

    if (!hasFragments && !top) {
        rootSpec = TypeSpec.classBuilder(rootClassName)
            .addModifiers(KModifier.DATA)
            .addAnnotation(Serializable::class)
        addPropertiesMutating(fieldGroup.fields, null, rootSpec)
    } else {
        rootSpec = TypeSpec.interfaceBuilder(rootClassName)
        val rootImplClassName = ClassName("", "${rootSpecTypeName}Impl")
        val rootImplSpec = TypeSpec.classBuilder(rootImplClassName)
            .addModifiers(KModifier.DATA)
            .addAnnotation(Serializable::class)
            .addSuperinterface(rootClassName)
        addPropertiesMutating(fieldGroup.fields, rootSpec, rootImplSpec)

        // always add the root impl spec
        rootSpec.addType(rootImplSpec.build())

        val rootSerializerClassName: ClassName

        if (hasFragments) {
            // group
            val fragmentsGroupClassName = ClassName("", FragmentsGroup)
            val fragmentsGroupClassSpec = TypeSpec
                .classBuilder(fragmentsGroupClassName)
                .addModifiers(KModifier.DATA)
                .addAnnotation(Serializable::class)
            val fragmentsGroupConstructorSpec = FunSpec.constructorBuilder()
            fieldGroup.fragmentSpreads.forEach {
                val propertyName = it.decapitalize()
                val propertyTypeName = ClassName(
                    config.packageName,
                    FragmentsContainer, it
                ).copy(nullable = true)
                fragmentsGroupConstructorSpec.addParameter(
                    parameterSpec(propertyName, propertyTypeName)
                        .defaultValue("null")
                        .build()
                )
                fragmentsGroupClassSpec.addProperty(
                    PropertySpec.builder(propertyName, propertyTypeName)
                        .initializer(propertyName)
                        .build()
                )
            }
            fragmentsGroupClassSpec.primaryConstructor(fragmentsGroupConstructorSpec.build())

            // adapter
            val fragmentsAdapterClassName = ClassName("", "$rootSpecTypeName$FragmentsAdapter")
            val fragmentsAdapterClassSpec = TypeSpec.classBuilder(fragmentsAdapterClassName)
                .addModifiers(KModifier.DATA)
                .addAnnotation(Serializable::class)
                .addSuperinterface(rootClassName, CodeBlock.of(FragmentsAdapterDelegateProperty))
                .addProperty(
                    PropertySpec.builder(FragmentsAdapterDelegateProperty, rootClassName).initializer(
                        FragmentsAdapterDelegateProperty
                    ).build()
                )
                .addProperty(
                    PropertySpec.builder(FragmentsAdapterFragmentsProperty, fragmentsGroupClassName).initializer(
                        FragmentsAdapterFragmentsProperty
                    ).build()
                )

            val fragmentsAdapterConstructorSpec = FunSpec.constructorBuilder()
                .addParameter(FragmentsAdapterDelegateProperty, rootClassName)
                .addParameter(
                    parameterSpec(
                        FragmentsAdapterFragmentsProperty, ClassName(
                            "",
                            FragmentsGroup
                        )
                    ).defaultValue("$FragmentsGroup()").build()
                )
            fragmentsAdapterClassSpec.primaryConstructor(fragmentsAdapterConstructorSpec.build())

            // adapter serializer
            val decoder = parameterSpec("decoder", Decoder::class).build()
            val encoder = parameterSpec("encoder", Encoder::class).build()
            val adapterObj = parameterSpec("obj", fragmentsAdapterClassName).build()
            val serializerSpec = TypeSpec.companionObjectBuilder()
                .addSuperinterface(KSerializer::class.asTypeName().parameterizedBy(fragmentsAdapterClassName))

            val unstableAnnotation =
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "UnstableDefault")).build()

            val deserializeFunSpec = FunSpec.builder("deserialize")
                .addAnnotation(unstableAnnotation)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(decoder)
                .returns(fragmentsAdapterClassName)
                .addStatement("val json = (%N as %T).decodeJson()", decoder, JsonInput::class)
                .addStatement(
                    "val delegate = %T.nonstrict.fromJson(%T.serializer(), json)",
                    Json::class.asClassName(),
                    rootImplClassName
                )


            val serializeFunSpec = FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addAnnotation(unstableAnnotation)
                .addParameter(encoder)
                .addParameter(adapterObj)
                .addStatement(
                    "val jsonObjects = mutableListOf<%T>(%T.nonstrict.toJson(%T.serializer(), obj.delegate as %T).jsonObject)",
                    JsonElement::class.asClassName(),
                    Json::class.asClassName(),
                    rootImplClassName,
                    rootImplClassName
                )
            fieldGroup.fragmentSpreads.forEach {
                val fragmentSpreadPropertyName = it.decapitalize()
                val fragmentSpreadClassName = ClassName(
                    config.packageName,
                    FragmentsContainer, it
                )

                // change from json to fromJsonOrNull
                deserializeFunSpec.addStatement(
                    "val $fragmentSpreadPropertyName = %T.nonstrict.fromJson(%T.Companion, json)",
                    Json::class.asClassName(),
                    fragmentSpreadClassName
                )
                serializeFunSpec.addStatement("if (obj.fragments.$fragmentSpreadPropertyName != null) {")
                    .addStatement(
                        "jsonObjects.add(%T.nonstrict.toJson(%T.Companion, obj.fragments.$fragmentSpreadPropertyName).jsonObject)",
                        Json::class.asClassName(),
                        fragmentSpreadClassName
                    )
                    .addStatement("}")

            }

            deserializeFunSpec.addStatement(
                "return %T(delegate, $FragmentsGroup(${fieldGroup.fragmentSpreads.joinToString(
                    ", "
                ) { it.decapitalize() }}))", fragmentsAdapterClassName
            )

            serializeFunSpec.addStatement("val jsonMap = mutableMapOf<String, %T>()", JsonElement::class.asClassName())
            serializeFunSpec.addStatement("jsonObjects.map { it.jsonObject }.forEach {jsonObject ->")
                .addStatement("\tjsonObject.keys.forEach {key ->")
                .addStatement("\t\tjsonMap[key] = jsonObject[key]!!")
                .addStatement("\t}")
                .addStatement("}")
                .addStatement(
                    "(encoder as %T).encodeJson(%T(jsonMap))",
                    JsonOutput::class,
                    JsonObject::class.asClassName()
                )


            // build serializer object spec
            serializerSpec.addProperty(
                PropertySpec.builder("descriptor", SerialDescriptor::class, KModifier.OVERRIDE)
                    .initializer(
                        CodeBlock.builder().addStatement(
                            "%T.%M(\"$rootSpecTypeName\")",
                            StringDescriptor::class.asClassName(),
                            MemberName("kotlinx.serialization", "withName")
                        ).build()
                    )
                    .build()

            )
                .addFunction(serializeFunSpec.build())
                .addFunction(deserializeFunSpec.build())

            // add serializer object spec to adapter
            fragmentsAdapterClassSpec.addType(serializerSpec.build())


            // add the fragment group and adapter to root
            rootSpec.addType(fragmentsAdapterClassSpec.build())
            rootSpec.addType(fragmentsGroupClassSpec.build())

            // set the root serializer to adapter serializer
            rootSerializerClassName = fragmentsAdapterClassName
        } else {
            // set the root serializer to rootImpl serializer
            rootSerializerClassName = rootImplClassName
        }

        // add rootspec serializer
        rootSpec
            .addAnnotation(Serializable::class)
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addAnnotation(
                        AnnotationSpec.builder(Serializer::class)
                            .addMember("forClass = %T::class", rootClassName)
                            .build()
                    )
                    .addProperty(
                        PropertySpec
                            .builder("descriptor", SerialDescriptor::class)
                            .addModifiers(KModifier.OVERRIDE)
                            .initializer("%T.serializer().descriptor", rootSerializerClassName)
                            .build()
                    )
                    .addFunction(
                        FunSpec
                            .builder("serialize")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("encoder", Encoder::class)
                            .addParameter("obj", rootClassName)
                            .addStatement(
                                "return %T.serializer().serialize(encoder, obj as %T)",
                                rootSerializerClassName,
                                rootSerializerClassName
                            )
                            .build()
                    )
                    .addFunction(
                        FunSpec
                            .builder("deserialize")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("decoder", Decoder::class)
                            .returns(rootClassName)
                            .addStatement("return %T.serializer().deserialize(decoder)", rootSerializerClassName)
                            .build()
                    )
                    .build()
            )

    }

    // nested complex fields
    val complexFields = fieldGroup.fields.filter { it.fields.isNotEmpty() }
    complexFields.forEach {
        processFieldGroup(it, rootSpec, config)
    }

    // add root spec to container once everything is processed.
    container.addType(rootSpec.build())
}
