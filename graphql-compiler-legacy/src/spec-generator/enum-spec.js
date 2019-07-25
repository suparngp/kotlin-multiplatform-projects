// @flow
import {
	camelCase,
	upperFirst
} from 'lodash';
import RootSpec from './root-spec';

export default class EnumSpec extends RootSpec {
	cases: string[];

	constructor(name: string) {
		super(name);
		this.cases = [];
		this.setSerializer(`${name}.Companion::class`);
	}

	addCase(value: string) {
		this.cases.push(value);
		return this;
	}

	addCases(values: string[]) {
		values.forEach(value => {
			this.addCase(value);
		});
		return this;

	}

	render() {
		const {
			cases,
			serializable,
			name
		} = this;
		const casesMap = cases.reduce((prev, next) => {
			prev[next] = upperFirst(camelCase(next.toLowerCase()));
			return prev;
		}, {});
		const decorators = this.renderDecorators();
		const body = serializable ? `
		companion object : KSerializer<${name}> {
			override val descriptor: SerialDescriptor =
					StringDescriptor.withName("${name}Serializer")

			override fun deserialize(decoder: Decoder): ${name} {
					return when (decoder.decodeString()) {
							${cases.map( value => `"${value}" -> ${casesMap[value]}`).join('\n')}
							else -> {
									throw Exception("Unknown Enum")
							}
					}
			}

			override fun serialize(encoder: Encoder, obj: ${name}) {
					encoder.encodeString(obj.value)
			}
	}
		` : '';
		return  `${decorators}
		enum class ${this.name}(val value: String) {
			${cases.map(name => `${casesMap[name]}("${name}")`).join(',\n')};${body}
		}
	`;
	}
}