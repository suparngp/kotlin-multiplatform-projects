// @flow
import Spec from './base';
import { prefixCount } from '../utils';
import { lowerFirst } from 'lodash';
export default class PropertySpec extends Spec {
	type: string;
	overridden: boolean;
	includedInConstructor: boolean;
	variable: boolean;
	value: any;
	originalName: string;

	constructor(name: string) {
		super(name);
		this.originalName = name;
		this.includedInConstructor = true;
	}

	ofType(type: string) {
		this.type = type;
		const { name } = this;
		if (type.startsWith(name)) {
			const stripped = lowerFirst(name.replace(/[_]+/gi, ''));
			const underscoreCount = prefixCount(name, '_');
			this.name = `${'_'.repeat(underscoreCount)}${stripped}`;
		}
		return this;
	}

	isVariable(variable: boolean) {
		this.variable = variable;
		return this;
	}

	isOverridden(overridden: boolean) {
		this.overridden = overridden;
		return this;
	}

	isIncludedInConstructor(included: boolean) {
		this.includedInConstructor = included;
		return this;
	}

	setValue(value: any) {
		this.value = value;
		return this;
	}

	renderAsPassToSuper() {
		return `${this.name}=${this.name}`;
	}

	renderAsArgument() {
		return `${this.name}: ${this.type}`;
	}

	render() {
		const { name, overridden, variable } = this;
		const decorators = this.renderDecorators();
		const overrideFlag = overridden ? 'override ' : '';
		const declarationType = variable ? 'var' : 'val';
		const type = this.type ? `: ${this.type}` : '';
		const value = this.value ? ` = ${this.value}` : '';
		const modifier = this.accessModifier || '';
		return `${decorators}${modifier} ${overrideFlag}${declarationType} ${name}${type}${value}`;
	}
}
