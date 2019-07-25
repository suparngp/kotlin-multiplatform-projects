## Entities

### Class

| attribute                    | description                                             |
| ---------------------------- | ------------------------------------------------------- |
| `name: [String]`             | name of the class                                       |
| `extends: [String]`          | names of all the parent classes                         |
| `implements: [String]`       | names of all the interfaces which this class implements |
| `properties: [Property]`     | list of all the properties                              |
| `inner: [Class | Interface]` | all the inner classes/interfaces                        |
| `isAbstract: Boolean`        | true of false if abstract                               |

### Interface

| attribute                    | description                                           |
| ---------------------------- | ----------------------------------------------------- |
| `name: [String]`             | name of the interface                                 |
| `extends: [Interface]`       | all the interfaces which this interface extends       |
| `properties: [Property]`     | all the properties of this interface                  |
| `possibleSubtypes: [String]` | all the possible types which implement this interface |
|                              |                                                       |
|                              |                                                       |
|                              |                                                       |

### Property

| attribute                        | description                                          |
| -------------------------------- | ---------------------------------------------------- |
| `name: [String]`                 | name of the property                                 |
| `type: [String]`                 | type of the property                                 |
| `isOverridden: Boolean`          | whether this property is overridden from the parent  |
| `includedInConstructor`: Boolean | whether this property is included in the constructor |
|                                  |                                                      |
|                                  |                                                      |
|                                  |                                                      |

### Fragment

A fragment has a typeCondition and possibleTypes.

`possibleTypes` means that this fragment can return multiple types of data. So you should check for the type name.

`typeCondition` means that this fragment operates on a particular type of data. It means all the possible types conform to the typeCondition. You need a deserializer based on the typeCondition.

A fragment field can use following types:

1. Primitives like string => Covered by the run time.
2. Pre-defined graphql types in Types.kt. => Covered because each of them is marked with @Serializable.
3. Nested Object => Implement as Serializable classes
4. Other fragments => Such fragments will extend other fragments. So

A nested Object's field can use following types:

1. Primitives like string => Covered by the run time.
2. Pre-defined graphql types in Types.kt. => Covered because each of them is marked with @Serializable.
3. Nested Object => Implement as Serializable classes
4. Other fragments =>

### Goal

If a fragment is an interface, then a query data object can implement many fragments without manual serialization/deserialization

You only need to deal with top level fragments and the first level nested objects. Any subsequent levels will be taken care automatically.

## Solutions

### Solution 1

#### General Rule

1. If there are no fragments in a type, create a data class with all the properties.
2. If a type refers to a fragment, then
   1. Create an interface for the type with a custom serializer based on "\_\_typename".
   2. Create a default implementation of just the interface. (This is not needed)
   3. For each fragment, create and implementation of the same name as the Fragment. This interface will conform to both the type and the Fragment interface which it refers to.

#### Passes

1. For each top level fragment, create an interface.
2. For each nested property,
   1. If there are no fragments references, then create a class with all the properties.
   2. Otherwise, create an interface with the same name as property.
   3. Create 