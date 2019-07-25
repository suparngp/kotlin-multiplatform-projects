// @flow
import { camelCase, upperFirst } from 'lodash';
import { ClassSpec, FileSpec, PropertySpec, InterfaceSpec } from '../spec-generator';
import { adjustType, defaultImports } from '../utils';
import processFieldGroups from './field-group';

const process = (input: RootInterface) => {
	const { operations } = input;
	const file = new FileSpec('Operations.kt')
		.inPackage('com.suparnatural')
		.withImports([
			...defaultImports,
			'import kotlinx.serialization.json.JsonElement',
			'import kotlinx.serialization.json.JsonObject',
			'import com.suparnatural.graphql.GraphQLOperation',
			'import com.suparnatural.graphql.fromJsonOrNull'
		])
		.inSourceDir(`${__dirname}/../out`);

	const operationSpecs: ClassSpec[] = operations.map(operation => {
		const { operationName, operationType } = operation;
		const name = upperFirst(camelCase(`${operationName}_${operationType}`));
		const operationSpec = new ClassSpec(name)
			.setStable(false)
			.setSerializable(true)
			// .setDataClass(true)
			.implementsInterface(new InterfaceSpec(`GraphQLOperation<${name}, ${name}.Data, Unit>`))
			.addBodyFragment(
				`
				override fun dataExtensionsSerializer(): KSerializer<Unit>? = null

				override fun dataSerializer(): KSerializer<Data> = Data.serializer()
		
				override fun requestSerializer(): KSerializer<${name}> = serializer()
				`
			)
			.addProperty(
				new PropertySpec('query')
					.ofType('String')
					.setValue(`"""${operation.sourceWithFragments.replace(/[$]/gi, '${"$"}')}"""`)
					.isIncludedInConstructor(false)
			);
		if (operation.variables && operation.variables.length) {
			const variables = operation.variables.map(({ name, type }) => new PropertySpec(name).ofType(adjustType(type)));
			new ClassSpec('Variables')
				.setDataClass(true)
				.setSerializable(true)
				.addProperties(variables)
				.containedBy(operationSpec);
			operationSpec.addProperty(new PropertySpec('variables').ofType('Variables'));
		}
		// const dataSpec = new ClassSpec('Data').setSerializable(true).setDataClass(true);
		(operation: any).type = 'Data';
		processFieldGroups(operation, operationSpec);
		// new ClassSpec('OperationResult')
		// 	.setDataClass(true)
		// 	.setStable(false)
		// 	.setSerializable(true)
		// 	.containedBy(operationSpec)
		// 	.addProperty(new PropertySpec('data').ofType('Data').isVariable(false));

		return operationSpec;
	});

	file.addRootSpecs(operationSpecs);
	return file;
};

export default process;
