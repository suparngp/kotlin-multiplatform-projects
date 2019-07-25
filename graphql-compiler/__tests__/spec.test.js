import { InterfaceSpec, ClassSpec } from '../src/spec-generator';
import FileSpec from '../src/spec-generator/file-spec';
import { exec } from 'child_process';

describe('InterfaceSpec', () => {
	// it('should render', () => {
	// 	const parent = new InterfaceSpec('Root');
	// 	const spec = new InterfaceSpec('Sample').setSerializable(true);
	// 	const subtype = new ClassSpec('SomeClass');
	// 	spec.extendsInterface(parent).hasSubtype(subtype).addProperty(new PropertySpec('property').ofType('String'));
	// 	console.log(spec.render());
	// });

	describe('ClassSpec', () => {
		// it('should render', () => {
		// 	const iface = new InterfaceSpec('SomeInterface')
		// 		.addProperty(new PropertySpec('interfaceProperty')
		// 			.ofType('String'));
		// 	const rootClass = new ClassSpec('RootClass').isExtendable(true)
		// 		.addProperty(new PropertySpec('rootClassProperty')
		// 			.ofType('String')
		// 			.isVariable(false));

			
		// 	const myclass = new ClassSpec('MyClass')
		// 		.extendsClass(rootClass)
		// 		.implementsInterface(iface)
		// 		.addProperty(new PropertySpec('MyProperty').ofType('String'));

		// 	const innerClass = new ClassSpec('InnerClass')
		// 		.addProperty(new PropertySpec('InnerProperty')
		// 			.ofType('Integer'))
		// 		.containedBy(myclass);
				
		// 	console.log(`${iface.render()}
		// 	${rootClass.render()}
		// 	${myclass.render()}`);
		// });

		it('deep nested spec should render', (done) => {
			const A = new InterfaceSpec('A');
			const B = new ClassSpec('B').containedBy(A);
			const C = new ClassSpec('C').containedBy(A);
			const D = new ClassSpec('D').isExtendable(true).containedBy(B);
			const E = new ClassSpec('E').containedBy(C).extendsClass(D); 
			const F = new InterfaceSpec('F');

			const File = new FileSpec('test.kt').inSourceDir(`${__dirname}/../out`)
				.inPackage('com.suparnatural')
				.addRootSpec(A)
				.addRootSpec(F);
			console.log(File.path());
			console.log(File.render());
			File.write();
			exec(`/Users/suparng/Code/personal/graphql-codegen/ktlint -F --experimental ${__dirname}/../out/**/*.kt`, (err, stdout, stderr) => {
				console.log(err, stdout, stderr);
				done();
			});
		});
	});
});