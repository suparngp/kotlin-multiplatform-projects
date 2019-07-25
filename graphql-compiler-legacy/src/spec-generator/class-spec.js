// @flow
import { flatten, cloneDeep, uniqWith } from 'lodash';
import type InterfaceSpec from './interface-spec';
import RootSpec from './root-spec';
import { PropertySpec } from '.';

export default class ClassSpec extends RootSpec {
	extendedFrom: string | ClassSpec;
	implementedFrom: InterfaceSpec[];
	isAbstract: boolean;
	extendable: boolean;
	accessName: string;
	isDataClass: boolean;

	interfaceDelegationMap: { [string]: PropertySpec };

	constructor(name: string) {
		super(name);
		this.isAbstract = false;
		this.implementedFrom = [];
		this.extendable = false;
		this.interfaceDelegationMap = {};
	}

	implementsInterface(iface: InterfaceSpec) {
		this.implementedFrom.push(iface);
		return this;
	}

	hasAccessName(accessName: string) {
		this.accessName = accessName;
		return this;
	}

	extendsClass(parentClass: ClassSpec) {
		if (typeof parentClass !== 'string' && !parentClass.extendable) {
			throw new Error(`${parentClass.name} is not extendable`);
		}
		this.extendedFrom = parentClass;
		return this;
	}

	isExtendable(extendable: boolean) {
		this.extendable = extendable;
		return this;
	}

	setAbstract(isAbstact: boolean) {
		this.isAbstract = isAbstact;
		return this;
	}

	setDataClass(isDataClass: boolean) {
		this.isDataClass = isDataClass;
		return this;
	}

	implementsInterfaceBy(iface: InterfaceSpec, by: PropertySpec) {
		this.implementsInterface(iface);
		this.interfaceDelegationMap[iface.accessName] = by;
	}

	render(): string {
		const { bodyFragments, isAbstract, accessModifier, isDataClass, name, children, extendable, implementedFrom, extendedFrom, properties, container, customSerializer } = this;
		const parentProperties = extendedFrom && typeof extendedFrom !== 'string' ? extendedFrom.properties : [];
		const parentPropertiesMap = parentProperties.reduce((prev, next) => {
			prev[next.name] = true;
			return prev;
		}, {});

		const interfaceProperties = uniqWith(flatten(implementedFrom.map(i => i.properties)).map(p => cloneDeep(p)), (p1, p2) => {
			return p1.name === p2.name && p1.type === p2.type;
		});
		interfaceProperties.forEach(p => {
			p.isOverridden(true);
		});
		const interfacePropertiesMap = interfaceProperties.reduce((prev, next) => {
			prev[next.name] = true;
			return prev;
		}, {});

		let ownProperties = [...interfaceProperties.filter(p => !parentPropertiesMap[p.name]), ...properties.filter(p => !parentPropertiesMap[p.name] && !interfacePropertiesMap[p.name])];

		let extensionHeader = '';
		if (extendedFrom) {
			if (typeof extendedFrom === 'string') {
				extensionHeader = extendedFrom;
			} else {
				const extensionName = extendedFrom === container || extendedFrom.container === container ? extendedFrom.name : extendedFrom.accessName;
				const extendedProperties = parentProperties
					.map(p => p.renderAsPassToSuper())
					.filter(Boolean)
					.join(', ');
				extensionHeader = `${extensionName}(${extendedProperties})`;
			}
		}

		const implementationHeader = implementedFrom
			.map(i => {
				const ifaceName = container === i || i.container === container ? i.name : i.accessName;
				const ifacePropertyMap = i.properties.reduce((prev, next) => {
					prev[next.name] = true;
					return prev;
				}, {});
				const delegate = this.interfaceDelegationMap[i.accessName];
				const by = delegate ? ` by ${delegate.name}` : '';

				// if delegated, then remove the iface properties from own properties.
				if (delegate) {
					ownProperties = ownProperties.filter(prop => !ifacePropertyMap[prop.name]);
				}

				return `${ifaceName}${by}`;
			})
			.filter(Boolean)
			.join(', ');
		if (name === 'SocialFeedbackSourceColorData') {
			// console.log('found', implementedFrom, ownProperties, interfaceProperties);
		}
		const propertiesHeader = [...parentProperties.map(p => p.renderAsArgument()), ...ownProperties.filter(p => p.includedInConstructor).map(p => p.render())].filter(Boolean).join(', ');
		const modifiers = [];
		if (extendable) {
			modifiers.push('open');
		}
		if (isDataClass) {
			modifiers.push('data');
		}

		if (isAbstract) {
			modifiers.push('abstract');
		}

		if (accessModifier === 'private') {
			modifiers.push('private');
		}
		const header = `${this.renderDecorators()}${modifiers.filter(Boolean).join(' ')} class ${name}${propertiesHeader ? `(${propertiesHeader})` : ''}`;
		const separator = extendedFrom || implementedFrom.length ? ': ' : '';
		const extensions = [extensionHeader, implementationHeader].filter(Boolean).join(', ');
		const childrenSpecs = ((Object.values(children): any): RootSpec[]);
		const body =
			bodyFragments.length || customSerializer || childrenSpecs.length
				? `{
			${this.renderCustomSerializer()}
			${childrenSpecs
		.map(i => i.render())
		.filter(Boolean)
		.join('\n')}
			${ownProperties
		.filter(p => !p.includedInConstructor)
		.map(p => p.render())
		.filter(Boolean)
		.join('\n')}

		${bodyFragments.join('\n')}
		}`
				: '';
		return `${header}${separator}${extensions}${body}`;
	}
}
