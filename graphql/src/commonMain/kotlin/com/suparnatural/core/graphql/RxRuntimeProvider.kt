package com.suparnatural.core.graphql

import com.suparnatural.core.rx.Observable
import com.suparnatural.core.rx.ObservableFactory
import com.suparnatural.core.rx.PublishSubject
import com.suparnatural.core.rx.PublishSubjectFactory

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

expect object RxRuntimeProvider {
    var observableFactory: ObservableFactory
    var publishSubjectFactory: PublishSubjectFactory
}