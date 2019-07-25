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
