package com.suparnatural.plugin.graphql.models

data class Arg (
        val name: String,
        val type: String,
        val value: Value
) {
    data class Value(
            val variableName: String,
            val kind: String
    )
}