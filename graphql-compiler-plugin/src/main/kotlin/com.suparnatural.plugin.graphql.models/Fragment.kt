package com.suparnatural.plugin.graphql.models


data class Fragment(
        val typeCondition: String,
        val possibleTypes: List<String>,
        val fragmentName: String,
        val filePath: String,
        val source: String,
        val fields: List<Field>,
        val fragmentSpreads: List<String>,
        val inlineFragments: List<InlineFragment>
)

data class InlineFragment(
        val typeCondition: String,
        val possibleTypes: List<String>,
        val fields: List<Field>,
        val fragmentSpreads: List<String>
)