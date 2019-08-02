package com.suparnatural.core.graphql

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GraphQLException(
        @SerialName("errorno") val errorNo: Int = 0,
        val code: String = "",
        @SerialName("syscall") val sysCall: String = "",
        val path: String = "",
        val stacktrace: List<String> = emptyList()
)

@Serializable
data class GraphQLErrorExtensions(val code: String = "", val exception: GraphQLException)

@Serializable
data class GraphQLError(
        val message: String = "",
        val path: List<String> = emptyList(),
        val extensions: GraphQLErrorExtensions
)