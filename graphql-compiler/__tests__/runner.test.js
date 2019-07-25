import input from '../input-2.json';

import { processFragments, processTypes, processOperations } from '../src/processor/index.js';
describe('runner', () => {
	it('should render types', () => {
		const types = processTypes(input);
		const fragments = processFragments(input);
		const operations = processOperations(input);
		types.write();
		fragments.write();
		operations.write();
		// console.log(types.render(), fragments.render(), operations.render());
		// console.log(types.render(), fragments.render());
	});
});
