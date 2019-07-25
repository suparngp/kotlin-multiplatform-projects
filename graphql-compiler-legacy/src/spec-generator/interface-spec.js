// @flow
import { flatten } from 'lodash';
import RootSpec from './root-spec';

export default class InterfaceSpec extends RootSpec {
	extendedFrom: InterfaceSpec[];
	possibleSubtypes: RootSpec[];

	serializable: boolean;

	constructor(name: string) {
		super(name);
		this.extendedFrom = [];
		this.possibleSubtypes = [];
	}

	extendsInterface(iface: InterfaceSpec) {
		this.extendedFrom.push(iface);
		return this;
	}

	addSubtype(subtype: RootSpec) {
		this.possibleSubtypes.push(subtype);
		return this;
	}

	addSubtypes(subtypes: RootSpec[]) {
		subtypes.forEach(subtype => {
			this.addSubtype(subtype);
		});
		return this;
	}

	renderHeader() {
		return `${this.name}`;
	}

	render(): string {
		const parentProperties = flatten(this.extendedFrom.map(e => e.properties));
		const parentPropertyNames = parentProperties.reduce((prev, next) => {
			prev[next.name] = true;
			return prev;
		}, {});
		const parents = this.extendedFrom.map(e => e.renderHeader()).join(', ');
		const { name } = this;
		const possibleTypes = this.possibleSubtypes.map(p => p.name);
		const companion = this.serializable
			? `
		companion object: KSerializer<${name}> {
			override val descriptor: SerialDescriptor = StringDescriptor.withName("${name}Serializer")

			override fun deserialize(decoder: Decoder): ${name} {
					val json = (decoder as JsonInput).decodeJson()

					return when ((json.jsonObject["__typename"] as JsonLiteral).content) {
							${possibleTypes.map(t => `"${t}" -> (JsonStrategy.fromJson(${t}.serializer(), json))`).join('\n')}

							else -> {
									throw Error("Unknown Possible Type for ${name}")
							}
					}
			}

			override fun serialize(encoder: Encoder, obj: ${name}) {
					val jsonEncoder = (encoder as JsonOutput)

					when (obj) {
							${possibleTypes.map(t => `is ${t} -> jsonEncoder.encodeJson(JsonStrategy.toJson(${t}.serializer(), obj))`).join('\n')}
							
							else -> {
								throw Error("Unknown Possible Type for ${name}")
							}
					}
			}
	}
		`
			: '';

		const ownProperties = this.properties.filter(prop => !parentPropertyNames[prop.name]);
		const properties = ownProperties.map(p => p.render()).join('\n');

		const body =
			this.bodyFragments.length || companion || properties || this.hasChildren()
				? `{
			${this.renderChildren()}
			${this.renderCustomSerializer()}
			${properties}
			${this.bodyFragments.join('\n')}
		}`
				: '';

		const decorators = this.renderDecorators();
		return `${decorators}interface ${this.name}${parents ? `: ${parents}` : ''}${body}`.trim();
	}
}
