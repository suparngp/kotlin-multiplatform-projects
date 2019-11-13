package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable


class NextLink(request: GraphQlOperation)
class RequestHandler(request: GraphQlOperation)

class Link(
        private val terminating: Boolean = false,
        val execute: (GraphQlOperation, ((GraphQlOperation) -> Observable<Any?>)?) -> Observable<Any?>
) {
    constructor(terminating: Boolean, link: Link) : this(terminating, link.execute)

    fun concat(nextLink: Link): Link {
        return Link(nextLink.terminating) {request, forward ->
            execute(request) {
                nextLink.execute(it, if(nextLink.terminating) null else forward)
            }
        }
    }
}

fun concatLinks(links: Array<Link>): Link {
    return links.reduce {prev, next ->
        prev.concat(next)
    }
}