import input from '../input.json';
import { processFragments } from '../src/processor';

describe('fragments', () => {
	it('should render', () => {
		console.log(processFragment(input).render());
	});
});