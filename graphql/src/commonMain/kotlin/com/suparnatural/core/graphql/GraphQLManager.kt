package com.suparnatural.core.graphql

import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

@UnstableDefault
interface GraphQLOperation<T, V, W> {
    fun requestSerializer(): KSerializer<T>
    fun dataSerializer(): KSerializer<V>
    fun dataExtensionsSerializer(): KSerializer<W>?
    fun serialize(): String {
        @Suppress("UNCHECKED_CAST")
        return Json.nonstrict.stringify(requestSerializer(), this as T)
    }
}

@UnstableDefault
data class HttpGraphQLResult<D : Any, E : Any>(
        val result: GraphQLOperationResult<D, E>,
        val httpStatusCode: Int,
        val httpHeaders: Map<String, String>
)


@UnstableDefault
interface GraphQLManager {
    fun <T : Any, D : Any, E : Any> execute(
            operation: GraphQLOperation<T, D, E>,
            resultCallback: (result: HttpGraphQLResult<D, E>) -> Unit
    )
}