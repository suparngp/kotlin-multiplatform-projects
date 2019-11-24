package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observableOf


class NextLink(request: GraphQlOperation)
class RequestHandler(request: GraphQlOperation)


interface Link<T, V> {
    val isTerminating: Boolean
    fun execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<T?>)?): Observable<V?>
}

class StringToIntLink: Link<String, Int> {
    override val isTerminating: Boolean = false

    override fun execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<String?>)?): Observable<Int?> {
        // update operation and call next
        return next?.invoke(operation)?.map { it?.length } ?: observableOf(0)
    }
}

class IntToBoolLink: Link<Int, Boolean> {
    override val isTerminating: Boolean = false

    override fun execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<Int?>)?): Observable<Boolean?> {
        // update operation

        return next?.invoke(operation)?.map { (it ?: 0) > 0 } ?: observableOf(false)
    }
}

class StringGenerator: Link<Nothing, String> {
    override val isTerminating: Boolean = true

    override fun execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<Nothing?>)?): Observable<String?> {
        return observableOf("Hello", "World", "Google", "Yahoo")
    }
}

class Link1: Link<String, Int> {
    override val isTerminating: Boolean = true

    override fun execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<String?>)?): Observable<Int?> {
        val firstLink = StringToIntLink()
        val secondLink = StringGenerator()

        return firstLink.execute(operation) {
            secondLink.execute(it, null)
        }
    }
}

class Link2: Link<Int, Boolean> {
    override val isTerminating: Boolean = true

    override fun execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<Int?>)?): Observable<Boolean?> {
        val firstLink = IntToBoolLink()
        val secondLink = Link1()

        return firstLink.execute(operation) {
            secondLink.execute(operation, null)
        }
    }
}


//fun <T, V, W>Link<T, V>.concat(secondLink: Link<V, W>): Link {
//    val firstLink = this
//    return object : Link {
//        override val isTerminating = secondLink.isTerminating
//
//        override fun <T> execute(operation: GraphQlOperation, next: ((GraphQlOperation) -> Observable<T?>)?): Observable<T?> {
//            return firstLink.execute(operation) {
//                secondLink.execute(it, if(secondLink.isTerminating) null else next)
//            }
//        }
//    }
//}