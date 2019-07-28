package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.*
import kotlinx.serialization.Serializable
import javax.naming.Name
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.Exception
import java.lang.reflect.Type

val KnownTypes = mutableMapOf<String, TypeName>(
        "Int" to INT,
        "Float" to FLOAT,
        "String" to STRING,
        "Boolean" to BOOLEAN,
        "ID" to TypeAliasSpec.builder("ID", String::class).build().type
)

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
                    val type = propertyTypeName(field.type, KnownTypes)
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

/*
// @flow

import { FileSpec, PropertySpec, ClassSpec, EnumSpec, TypeAliasSpec } from '../spec-generator';
import { adjustType, defaultImports } from '../utils';

const process = (input: any) => {
	const { typesUsed } = input;
	const file = new FileSpec('types.kt')
		.inSourceDir(`${__dirname}/../out`)
		.withImports(defaultImports)
		.inPackage('com.suparnatural');

	file.addRootSpec(new TypeAliasSpec('ID').setValue('String'));

	const result = typesUsed.map(({ name, fields, values, kind }) => {
		if (kind === 'InputObjectType') {
			const properties = fields.map(field => new PropertySpec(field.name).ofType(adjustType(field.type)));
			return new ClassSpec(name)
				.setDataClass(true)
				.setSerializable(true)
				.addProperties(properties);
		} else if (kind === 'EnumType') {
			return new EnumSpec(name).setSerializable(true).addCases(values.map(v => v.name));
		} else if (kind === 'ScalarType') {
			return new ClassSpec(name).setSerializable(true);
		}
		throw new Error(`Unknown kind ${kind} in types used`);
	});
	file.addRootSpecs(result);
	return file;
};

export default process;
 */