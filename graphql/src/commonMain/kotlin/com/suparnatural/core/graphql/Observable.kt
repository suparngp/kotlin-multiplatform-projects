package com.suparnatural.core.graphql

interface Observable<T> {
    fun <V> map(project: (value: T) -> V): Observable<V>
    fun filter(predicate: (value: T) -> Boolean): Observable<T>
    fun reduce(accumulator: (acc: T, value: T) -> T, seed: T?): Observable<T>
    fun subscribe(subscriber: (value: T) -> Unit)
}

interface ObservableFactory {
    fun<T> of(vararg values: T): Observable<T>
}

interface PublishSubject<T> {
    fun asObservable(): Observable<T>
    fun onNext(value: T)
    fun onComplete()
    fun onError(error: Throwable)
}

interface PublishSubjectFactory {
    fun<T> create(): PublishSubject<T>
}