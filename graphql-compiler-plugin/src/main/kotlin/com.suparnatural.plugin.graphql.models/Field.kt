package com.suparnatural.plugin.graphql.models

data class Field(
        val responseName: String,
        val fieldName: String,
        val type: String,
        val args: List<Arg> = emptyList(),
        val fields: List<Field> = emptyList(),
        val isConditional: Boolean,
        val isDeprecated: Boolean,
        val fragmentSpreads: List<String> = emptyList(),
        val inlineFragments: List<InlineFragment> = emptyList()
)