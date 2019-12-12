package com.suparnatural.runtime.rx

import com.badoo.reaktive.maybe.asObservable
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.subject.publish.publishSubject
import com.suparnatural.core.rx.Observable
import com.suparnatural.core.rx.ObservableFactory
import com.suparnatural.core.rx.PublishSubject
import com.suparnatural.core.rx.PublishSubjectFactory


class BadooObservable<T>(private val observable: com.badoo.reaktive.observable.Observable<T>) : Observable<T> {
    override fun <V> map(project: (value: T) -> V): Observable<V> {
        return BadooObservable(observable.map(project))
    }

    override fun filter(predicate: (value: T) -> Boolean): Observable<T> {
        return BadooObservable(observable.filter(predicate))
    }

    override fun reduce(accumulator: (acc: T, value: T) -> T, seed: T?): Observable<T> {
        return BadooObservable(observable.reduce(accumulator).asObservable())
    }

    override fun subscribe(subscriber: (value: T) -> Unit) {
        observable.subscribe(false, null, null, null, subscriber)
    }
}

class BadooPublishSubject<T>(private val subject: com.badoo.reaktive.subject.publish.PublishSubject<T>) : PublishSubject<T> {
    override fun asObservable(): Observable<T> {
        return BadooObservable(subject)
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

class BadooObservableFactory : ObservableFactory {
    override fun <T> of(vararg values: T): Observable<T> {
        return BadooObservable(observableOf(*values))
    }
}

class BadooPublishSubjectFactory : PublishSubjectFactory {
    override fun <T> create(): PublishSubject<T> {
        return BadooPublishSubject(publishSubject())
    }
}