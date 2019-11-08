package com.suparnatural.core.graphql.legacy

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

@UnstableDefault
open class DefaultGraphQLManager(private val graphQLClient: GraphQLClient) : GraphQLManager {
    override fun <T : Any, D : Any, E : Any> execute(
            operation: GraphQLOperation<T, D, E>,
            resultCallback: (result: HttpGraphQLResult<D, E>) -> Unit
    ) {
//        val request = GraphQLClientRequest(operation.serialize(),  mapOf("Accept" to "application/json", "Content-Type" to "application/json"))
//        val mainCallback = ThreadTransferableJob.create(Unit, resultCallback)
//
//        graphQLClient.execute(request) {
//            val result = Json.nonstrict.parse(
//                    GraphQLOperationResultSerializer(
//                            operation.dataSerializer(),
//                            operation.dataExtensionsSerializer()
//                    ), it.jsonResponse
//            )
//            mainCallback.job(HttpGraphQLResult(result, it.httpStatusCode, it.httpResponseHeaders))
//        }
    }
}

