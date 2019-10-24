package com.suparnatural.plugin.graphql.models

enum class OperationType {
    query,
    mutation
}

data class NameType(val name: String, val type: String)

data class Operation(
        val filePath: String,
        val operationId: String,
        val source: String,
        val sourceWithFragments: String,
        val operationName: String,
        val operationType: OperationType,
        val rootType: String,
        val variables: List<NameType> = emptyList(),
        val fragmentsReferenced: List<String> = emptyList(),
        override val fields: List<Field> = emptyList(),
        override val fragmentSpreads: List<String> = emptyList(),
        override val inlineFragments: List<InlineFragment> = emptyList(),
        override val typeName: String = operationName
): FieldGroup