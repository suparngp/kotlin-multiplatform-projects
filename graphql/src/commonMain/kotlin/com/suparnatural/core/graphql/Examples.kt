package com.suparnatural.core.graphql

import com.suparnatural.core.rx.Observable

class StringGeneratorLink : Link<Unit, Unit, String> {
    override fun execute(operation: Unit, next: Link<Unit, *, Unit>?): Observable<String> {
        println("Inside string generator")
        return RxRuntimeProvider.observableFactory.of("Hello", "World")
    }
}

class StringToIntLink : Link<Unit, String, Int> {
    override fun execute(operation: Unit, next: Link<Unit, *, String>?): Observable<Int> {
        // modify the operation
        println("inside string to int")
        return (next?.execute(operation) ?: RxRuntimeProvider.observableFactory.of<String?>(null)).map {
            println("found string $it")
            it?.length ?: 0
        }
    }
}

class EvenOddLink : Link<Unit, Int, Boolean> {
    override fun execute(operation: Unit, next: Link<Unit, *, Int>?): Observable<Boolean> {
        // modify the operation

        return (next?.execute(operation) ?: RxRuntimeProvider.observableFactory.of<Int?>(null)).map {
            println("Found int $it")
            (it ?: 0) % 2 == 0
        }
    }

}


class NativeFetcher : JsonHttpFetcher {
    override fun fetch(request: JsonHttpFetchRequest, handler: (JsonHttpFetchResponse) -> Unit) {
        handler(JsonHttpFetchResponse("", 200))
    }
}