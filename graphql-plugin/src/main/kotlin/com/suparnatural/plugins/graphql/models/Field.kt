package com.suparnatural.plugins.graphql.models

data class Field(
        val responseName: String = "",
        val fieldName: String = "",
        val description: String = "",
        val type: String = "",
        val args: List<Arg> = emptyList(),
        val isConditional: Boolean = false,
        val isDeprecated: Boolean = false,
        override val fields: List<Field> = emptyList(),
        override val fragmentSpreads: List<String> = emptyList(),
        override val inlineFragments: List<InlineFragment> = emptyList(),
        override val typeName: String = type
): FieldGroup