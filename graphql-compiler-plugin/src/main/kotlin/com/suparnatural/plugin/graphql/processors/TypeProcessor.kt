package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.*
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.EnumValue
import com.suparnatural.plugin.graphql.models.Field
import com.suparnatural.plugin.graphql.models.TypeUsed
import com.suparnatural.plugin.graphql.models.TypeUsedKind
import kotlinx.serialization.Serializable

fun TypeSpec.Builder.addFieldsAsProperties(fields: List<Field>) {
    fields.forEach {
        addProperty(it.responseName, ClassName("", propertyType(it.type)))
    }
}

fun addProperties(fields: List<Field>, container: TypeSpec.Builder) {
    val specs = mutableListOf<PropertySpec>()

    fields.forEach {
        val className = ClassName("", propertyType(it.type))
        val spec = PropertySpec.builder(it.responseName, className)

        if (it.fields.isNotEmpty()) {
//            container.addType(
//                    TypeSpec.classBuilder(snakeToPascal(it.type))
//            )
        }
    }
}

//fun properties(fields: List<Field>): List<PropertySpec> {
//    return fields.map {
//        val spec = PropertySpec.builder(it.responseName, it.type)
//    }
//}

fun TypeSpec.Builder.addEnumConstants(names: List<EnumValue>): TypeSpec.Builder {
    names.forEach {
//        addEnumConstant(snakeToCamel(it.name))
    }
    return this
}


fun specForType(type: TypeUsed): TypeSpec {
    val spec = when(type.kind) {
        TypeUsedKind.InputObjectType -> {
            TypeSpec.classBuilder(type.name)
                    .addModifiers(KModifier.DATA)


        }
        TypeUsedKind.ScalarType -> {
            TypeSpec.classBuilder(type.name)
        }
        TypeUsedKind.EnumType -> {
            TypeSpec.enumBuilder(type.name)
                    .addEnumConstants(type.values)


        }
    }
    spec.addAnnotation(Serializable::class)
    return spec.build()
}

fun processTypes(types: List<TypeUsed>, config: SuparnaturalGraphqlExtension): FileSpec {
    val fileSpec = FileSpec
            .builder(config.packageName, "Types")
            .addTypeAlias(TypeAliasSpec.builder("ID", String::class).build())

    types.forEach {
        fileSpec.addType(specForType(it))
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