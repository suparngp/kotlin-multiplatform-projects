package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.behavior.behaviorSubject
import com.badoo.reaktive.subject.publish.publishSubject

typealias JsonBody = String


interface GraphQlTransport {

    data class Response(val isSuccess: Boolean, val httpStatusCode: Int, val httpStatusMessage: String, val body: JsonBody? = null, val headers: Map<String, String>? = null)
    fun transport(request: GraphQlOperation.Request, responseHandler: (Response) -> Void)
}

class GraphQlClient(private val linkChain: Link) {

    fun <T: GraphQlOperation> execute(operation: T): Observable<T> {
        val subject = publishSubject<T>()
        linkChain.execute(operation, null).subscribe { result: Any? ->

        }

        return subject
    }
}