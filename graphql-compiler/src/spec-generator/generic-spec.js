// @flow

import Spec from './base';
export class GenericSpec extends Spec {
	content: string;

	setContent(content: string) {
		this.content = content;
		return this;
	}

	render() {
		return this.content;
	}
}
