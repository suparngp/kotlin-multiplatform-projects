package com.suparnatural.core.graphql

internal class EmptyObservableFactory : ObservableFactory {
    override fun <T> of(vararg values: T): Observable<T> {
        throw Exception("Missing rx-runtime")
    }

}

internal class EmptyPublishSubjectFactory : PublishSubjectFactory {
    override fun <T> create(): PublishSubject<T> {
        throw Exception("Missing rx-runtime")
    }

}

object RxRuntimeProvider {
    var observableFactory: ObservableFactory = EmptyObservableFactory()
    var publishSubjectFactory: PublishSubjectFactory = EmptyPublishSubjectFactory()
}