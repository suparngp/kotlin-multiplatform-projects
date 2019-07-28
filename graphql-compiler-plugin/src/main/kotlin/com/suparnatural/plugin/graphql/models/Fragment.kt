package com.suparnatural.plugin.graphql.models


data class Fragment(
        val typeCondition: String = "",
        val possibleTypes: List<String> = emptyList(),
        val fragmentName: String = "",
        val filePath: String = "",
        val source: String = "",
        val fields: List<Field> = emptyList(),
        val fragmentSpreads: List<String> = emptyList(),
        val inlineFragments: List<InlineFragment> = emptyList()
)

data class InlineFragment(
        val typeCondition: String = "",
        val possibleTypes: List<String> = emptyList(),
        val fields: List<Field> = emptyList(),
        val fragmentSpreads: List<String> = emptyList()
)