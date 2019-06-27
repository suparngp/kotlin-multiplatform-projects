package com.suparnatural.core.graphql


data class GraphQLClientRequest(val jsonBody: String, val httpHeaders: Map<String, String>)

data class GraphQLClientResponse(val jsonResponse: String, val httpStatusCode: Int, val httpResponseHeaders: Map<String, String>)

interface GraphQLClient {
    fun execute(
            request: GraphQLClientRequest, callback: (GraphQLClientResponse) -> Unit
    )
}