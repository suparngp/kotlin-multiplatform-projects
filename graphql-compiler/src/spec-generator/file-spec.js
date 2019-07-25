// @flow

import Spec from './base';
import fs from 'fs-extra';
import type RootSpec from './root-spec';
import path from 'path';
import { GenericSpec } from './generic-spec';

export default class FileSpec extends Spec {
	packageName: string;
	imported: string[];
	dir: string;
	rootSpecs: RootSpec[];
	genericSpecs: GenericSpec[];

	constructor(name: string) {
		super(name);
		this.imported = [];
		this.rootSpecs = [];
		this.genericSpecs = [];
	}

	addRootSpec(rootSpec: RootSpec) {
		this.rootSpecs.push(rootSpec);
		return this;
	}

	addRootSpecs(rootSpecs: RootSpec[]) {
		rootSpecs.forEach(rootSpec => {
			this.addRootSpec(rootSpec);
		});
		return this;
	}

	inSourceDir(dir: string) {
		this.dir = dir;
		return this;
	}

	inPackage(packageName: string) {
		this.packageName = packageName;
		return this;
	}

	withImports(imports: string[]) {
		this.imported = imports;
		return this;
	}

	addGenericSpec(genericSpec: GenericSpec) {
		this.genericSpecs.push(genericSpec);
		return this;
	}

	render() {
		const genericSpecs = this.genericSpecs.map(g => g.render()).join('\n');
		const content = this.rootSpecs.map(r => r.render()).join('\n');
		return `
		package ${this.packageName}

		${this.imported.join('\n')}

		${genericSpecs}

		${content}
		`;
	}

	path() {
		return path.join(this.dir, this.packageName.replace(/[.]+/gi, '/'), `${this.name.replace('.kt', '')}.kt`);
	}

	write() {
		const dest = this.path();
		const content = this.render();
		fs.ensureFileSync(dest);
		fs.writeFileSync(dest, content, { encoding: 'utf-8' });
	}
}
