// @flow
import Spec from './base';

export default class TypeAliasSpec extends Spec {
  value: string;
  setValue(value: string) {
  	this.value = value;
  	return this;
  }
  
  render() {
  	return `typealias ${this.name} = ${this.value}`;
  }
}