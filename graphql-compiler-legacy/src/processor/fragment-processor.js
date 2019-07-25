// @flow

import { ClassSpec, InterfaceSpec } from '../spec-generator';
import { defaultImpl, fieldsToProperties, FragmentsContainer, wrapperPath } from '../utils';
import processFieldGroup from './field-group';

// eslint-disable-next-line no-undef
const process = (fragments: Fragment[]) => {
	// Create top Fragments container interface
	const root = new InterfaceSpec(FragmentsContainer).setStable(false);
	const fragmentSpecs = fragments.map(fragment => {
		const spec = new InterfaceSpec(fragment.fragmentName).containedBy(root);
		const properties = fieldsToProperties(fragment.fields);
		spec.addProperties(properties);

		// const default implementation
		new ClassSpec(defaultImpl(fragment.fragmentName))
			.implementsInterface(spec)
			.containedBy(spec)
			.setSerializable(true)
			.setDataClass(true)
			.addProperties(fieldsToProperties(fragment.fields));

		if (fragment.fragmentSpreads.length || fragment.inlineFragments.length) {
			spec.setSerializer(`${wrapperPath(fragment.fragmentName)}.Companion::class`);
		}

		return spec;
	});

	fragments.forEach((fragment, index) => {
		const complexFields = fragment.fields.filter(field => !!field.fields);
		const containerSpec = fragmentSpecs[index];
		complexFields.forEach(fieldGroup => processFieldGroup(fieldGroup, containerSpec));
	});

	return root;
};

export default process;
