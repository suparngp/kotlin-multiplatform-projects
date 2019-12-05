package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.publish.publishSubject
import kotlinx.serialization.UnstableDefault

open class GraphQlHttpResponse(override val body: String?, val httpStatusCode: Int, val httpStatusMessage: String? = null): GraphQlFetcher.Response

open class GraphQlHttpLink(private val fetcher: GraphQlFetcher<GraphQlHttpResponse>, private val options: Options): Link<GraphQlOperation<*>, Unit, GraphQlFetcher.Response> {

    data class Options(val url: String, val defaultHeaders: Map<String, String> = emptyMap())

    private val contextKeyHeaders = "headers"

    @UnstableDefault
    override fun execute(operation: GraphQlOperation<*>, next: Link<GraphQlOperation<*>, *, Unit>?): Observable<GraphQlFetcher.Response> {
        val subject = publishSubject<GraphQlHttpResponse>()
        val headers = options.defaultHeaders.toMutableMap()
        if (operation.context.containsKey(contextKeyHeaders) ) {
            val overrides = operation.context[contextKeyHeaders]
            if (overrides is Map<*, *>) {
                overrides.entries.forEach {
                    headers[it.key as String] = it.value as String
                }
            }
        }
        fetcher.fetch(options.url, operation.serializedString, headers) {
            if (it.httpStatusCode != 200 || it.body.isNullOrEmpty()) {
                subject.onError(Exception("Invalid Http Response"))
            } else {
                subject.onNext(it)
                subject.onComplete()
            }
        }
        return subject
    }

}