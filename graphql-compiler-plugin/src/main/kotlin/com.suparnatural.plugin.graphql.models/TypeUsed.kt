package com.suparnatural.plugin.graphql.models
enum class TypeUsedKind {
    InputObjectType,
    ScalarType,
    EnumType
}

data class EnumValue(val name: String, val isDeprecated: Boolean)
data class TypeUsed(
        val kind: TypeUsedKind,
        val name: String,
        val fields: List<NameType>,
        val description: String = "",
        val values: List<EnumValue> = emptyList()
)

