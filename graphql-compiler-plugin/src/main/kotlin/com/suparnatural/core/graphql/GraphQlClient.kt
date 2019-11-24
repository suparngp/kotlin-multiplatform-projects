package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.behaviorSubject
import kotlinx.serialization.UnstableDefault

typealias JsonBody = String


interface GraphQlHttpFetcher {

    data class Response(val isSuccess: Boolean, val httpStatusCode: Int, val httpStatusMessage: String, val body: JsonBody? = null, val headers: Map<String, String>? = null)
    fun transport(request: GraphQlRequest, responseHandler: (Response) -> Void)

    fun fetch(url: String, request: GraphQlRequest, headers: Map<String, String>?, handler: (Response) -> Void)
}

data class GraphQlHttpFetcherOptions(val headers: Map<String, String>?)

interface GraphQlClient {
    fun <T: GraphQlOperation> execute(operation: T, linkChain: Link): Observable<T>
}

class GraphQlHttpLink(val url: String, val httpFetcher: GraphQlHttpFetcher, val defaultHeaders: Map<String, String>? = null): Link {
    override val isTerminating = true

    @UnstableDefault
    override fun <T> execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<T?>)?): Observable<T?> {
        val subject = behaviorSubject<T?>(null)

        val request = operation.request
        val requestBody = request.serialize()

        httpFetcher.fetch(url, request, defaultHeaders) {
            subject.onNext(it)
        }

        return subject
    }
}