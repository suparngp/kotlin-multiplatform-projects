package com.suparnatural.plugin.graphql.models


data class Fragment(
        val typeCondition: String = "",
        val possibleTypes: List<String> = emptyList(),
        val fragmentName: String = "",
        val filePath: String = "",
        val source: String = "",
        override val fields: List<Field> = emptyList(),
        override val fragmentSpreads: List<String> = emptyList(),
        override val inlineFragments: List<InlineFragment> = emptyList(),
        override val typeName: String = fragmentName
): FieldGroup

data class InlineFragment(
        val typeCondition: String = "",
        val possibleTypes: List<String> = emptyList(),
        val fields: List<Field> = emptyList(),
        val fragmentSpreads: List<String> = emptyList()
)