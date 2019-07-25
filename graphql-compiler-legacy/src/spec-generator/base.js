// @flow
class Spec {
	name: string;
	stable: boolean;
	serializable: boolean;
	serializer: string;
	accessModifier: AccessModifier;

	constructor(name: string) {
		this.name = name;
		this.stable = true;
	}

	setStable(stable: boolean) {
		this.stable = stable;
		return this;
	}

	setSerializable(serializable: boolean) {
		this.serializable = serializable;
		return this;
	}

	setSerializer(serializer: string) {
		this.serializable = true;
		this.serializer = serializer;
		return this;
	}

	setAccessModifier(modifier: AccessModifier) {
		this.accessModifier = modifier;
		return this;
	}

	renderDecorators() {
		const decorators = [];
		if (!this.stable) {
			decorators.push('@UnstableDefault');
		}
		if (this.serializable) {
			decorators.push(`@Serializable${this.serializer ? `(with = ${this.serializer})` : ''}`);
		}
		decorators.push('');
		return decorators.join('\n');
	}
}

export default Spec;
