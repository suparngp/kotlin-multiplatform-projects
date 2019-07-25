// @flow
import Spec from './base';
import type PropertySpec from './property-spec';

export default class RootSpec extends Spec {
	container: RootSpec;
	properties: PropertySpec[];
	children: { [string]: RootSpec };
	customSerializer: string;
	bodyFragments: string[];

	constructor(name: string) {
		super(name);
		this.properties = [];
		this.children = {};
		this.bodyFragments = [];
	}
	get accessName() {
		let container = this.container;
		const path = [this.name];
		while (container) {
			path.push(container.name);
			container = container.container;
		}
		return path.reverse().join('.');
	}

	containedBy(container: RootSpec) {
		this.container = container;
		container.children[this.name] = this;
		return this;
	}

	addProperty(prop: PropertySpec) {
		this.properties.push(prop);
		return this;
	}

	addProperties(props: PropertySpec[]) {
		props.forEach(prop => {
			this.properties.push(prop);
		});
		return this;
	}

	get hasProperties() {
		return this.properties.length;
	}

	hasChildren() {
		return Object.keys(this.children).length > 0;
	}

	setCustomSerializer(customSerializer: string) {
		this.customSerializer = customSerializer;
		return this;
	}

	addBodyFragment(fragment: string) {
		this.bodyFragments.push(fragment);
		return this;
	}

	renderCustomSerializer() {
		return this.customSerializer ? this.customSerializer : '';
	}

	renderChildren() {
		const childrenSpecs = ((Object.values(this.children): any): RootSpec[]);
		return childrenSpecs.length
			? `
			${childrenSpecs
		.map(i => i.render())
		.filter(Boolean)
		.join('\n')}
		`
			: '';
	}

	render() {
		return '';
	}
}
