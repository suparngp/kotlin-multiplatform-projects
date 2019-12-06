package com.suparnatural.plugins.graphql.models

interface FieldGroup {
    val fields: List<Field>
    val fragmentSpreads: List<String>
    val inlineFragments: List<InlineFragment>
    val typeName: String
}