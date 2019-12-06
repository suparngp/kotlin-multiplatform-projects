package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.publish.publishSubject
import kotlinx.serialization.UnstableDefault

open class JsonHttpGraphQlLink(
        val fetcher: JsonHttpFetcher,
        val url: String,
        val defaultHeaders: Map<String, String> = emptyMap()
) : Link<GraphQlOperation<*>, Unit, JsonHttpFetchResponse> {

    private val contextKeyHeaders = "headers"

    @UnstableDefault
    override fun execute(
            operation: GraphQlOperation<*>,
            next: Link<GraphQlOperation<*>, *, Unit>?
    ): Observable<JsonHttpFetchResponse> {
        val subject = publishSubject<JsonHttpFetchResponse>()
        val headers = defaultHeaders.toMutableMap()
        if (operation.context.containsKey(contextKeyHeaders)) {
            val overrides = operation.context[contextKeyHeaders]
            if (overrides is Map<*, *>) {
                overrides.entries.forEach {
                    headers[it.key as String] = it.value as String
                }
            }
        }
        fetcher.fetch(url, JsonHttpFetchRequest(operation.serializedString), headers) {
            subject.onNext(it)
            subject.onComplete()
        }
        return subject
    }

}