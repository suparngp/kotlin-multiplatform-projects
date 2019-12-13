package com.suparnatural.runtime.rx.reaktive

import com.badoo.reaktive.maybe.asObservable
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.subject.publish.publishSubject
import com.suparnatural.core.rx.Observable
import com.suparnatural.core.rx.ObservableFactory
import com.suparnatural.core.rx.PublishSubject
import com.suparnatural.core.rx.PublishSubjectFactory


class ReaktiveObservable<T>(private val observable: com.badoo.reaktive.observable.Observable<T>) : Observable<T> {
    override fun <V> map(project: (value: T) -> V): Observable<V> {
        return ReaktiveObservable(observable.map(project))
    }

    override fun filter(predicate: (value: T) -> Boolean): Observable<T> {
        return ReaktiveObservable(observable.filter(predicate))
    }

    override fun reduce(accumulator: (acc: T, value: T) -> T, seed: T?): Observable<T> {
        return ReaktiveObservable(observable.reduce(accumulator).asObservable())
    }

    override fun subscribe(subscriber: (value: T) -> Unit) {
        observable.subscribe(false, null, null, null, subscriber)
    }
}

class ReaktivePublishSubject<T>(private val subject: com.badoo.reaktive.subject.publish.PublishSubject<T>) : PublishSubject<T> {
    override fun asObservable(): Observable<T> {
        return ReaktiveObservable(subject)
    }

    override fun onNext(value: T) {
        subject.onNext(value)
    }

    override fun onComplete() {
        subject.onComplete()
    }

    override fun onError(error: Throwable) {
        subject.onError(error)
    }
}

class ReaktiveObservableFactory : ObservableFactory {
    override fun <T> of(vararg values: T): Observable<T> {
        return ReaktiveObservable(observableOf(*values))
    }
}

class ReaktivePublishSubjectFactory : PublishSubjectFactory {
    override fun <T> create(): PublishSubject<T> {
        return ReaktivePublishSubject(publishSubject())
    }
}