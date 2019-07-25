// @flow
// export type RootSpec = ClassSpec | InterfaceSpec;
import input from '../input.json';

// eslint-disable-next-line no-undef
export type Input = $Shape<input>;
export type AccessModifier = 'private' | 'public';

export type Field = {
	responseName: string,
	fieldName: string,
	type: string,
	isConditional: boolean,
	isDeprecated?: boolean,
	fields?: Field[],
	fragmentSpreads?: any[],
	inlineFragments?: any[]
};

export type Field1 = {
	responseName: string,
	fieldName: string,
	type: string,
	isConditional: boolean
};

export type Field2 = {
	name: string,
	type: string
};

export type Fragment = {
	typeCondition: string,
	possibleTypes: string[],
	fragmentName: string,
	filePath: string,
	source: string,
	fields: Field[],
	fragmentSpreads: any[],
	inlineFragments: any[]
};

export type Operation = {
	filePath: string,
	operationName: string,
	operationType: string,
	rootType: string,
	variables: any[],
	source: string,
	fields: Field[],
	fragmentSpreads: any[],
	inlineFragments: any[],
	fragmentsReferenced: string[],
	sourceWithFragments: string,
	operationId: string
};

export type RootInterface = {
	operations: Operation[],
	fragments: Fragment[],
	typesUsed: TypesUsed[]
};

export type TypesUsed = {
	kind: string,
	name: string,
	fields?: Field2[],
	description?: string,
	values?: Value[]
};

type Value = {
	name: string,
	isDeprecated: boolean
};
