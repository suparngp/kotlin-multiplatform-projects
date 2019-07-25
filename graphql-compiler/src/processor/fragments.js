// @flow
import { FileSpec } from '../spec-generator';
import { defaultImports } from '../utils';
import processFieldGroup from './fragment-processor';

const process = (input: any) => {
	const { fragments } = input;
	const file = new FileSpec('Fragments.kt')
		.inSourceDir(`${__dirname}/../out`)
		.withImports([...defaultImports, 'import com.suparnatural.graphql.fromJsonOrNull', 'import kotlinx.serialization.json.JsonElement', 'import kotlinx.serialization.json.JsonObject'])
		.inPackage('com.suparnatural');

	file.addRootSpec(processFieldGroup(fragments));
	return file;
};

export default process;
