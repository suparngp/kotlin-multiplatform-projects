import fs from 'fs';

import { processNode } from './type-processor';
import { processOperation } from './operation-processor';
import { processFragment } from './fragment-processor';
const main = (args) => {
	const inputFile = args[2];
	const outputFile = args[3];
	const data = require(inputFile);
	const lines = [`import kotlinx.serialization.internal.StringDescriptor
	import kotlinx.serialization.json.Json
	import kotlinx.serialization.json.JsonInput
	import kotlinx.serialization.json.JsonLiteral
	import kotlinx.serialization.json.JsonOutput`, 'typealias ID = String', 'val JsonStrategy = Json.nonstrict'];
	fs.truncateSync(outputFile, 0);
  
	data.typesUsed.forEach(node => {
		lines.push(processNode(node));
	});
  
	data.fragments.forEach(fragment => {
		lines.push(processFragment(fragment));
	});

	data.operations.forEach(operation => {
		lines.push(processOperation(operation, data.fragments.reduce((prev, fragment) => {
			prev[fragment.fragmentName] = fragment;
			return prev;
		}, {})));
	});

	// lines.push(processOperation(data.operations[1]));
	fs.writeFileSync(outputFile, lines.filter(Boolean).join('\n'), {flag: 'a'});
};

main(process.argv);