package com.suparnatural.plugins.graphql.models

data class Arg (
        val name: String = "",
        val type: String = "",
        val value: Any
) {
//    data class Value(
//            val variableName: String,
//            val kind: String
//    )
}