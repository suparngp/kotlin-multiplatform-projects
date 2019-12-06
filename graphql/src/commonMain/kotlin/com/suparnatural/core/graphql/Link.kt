package com.suparnatural.core.graphql

import com.badoo.reaktive.observable.Observable


interface Link<A, T, V> {
    fun execute(operation: A, next: Link<A, *, T>? = null): Observable<V>
}

fun <A, T, V> Link<A, T, V>.concat(nextLink: Link<A, *, T>): Link<A, T, V> {
    val firstLink = this;
    return object : Link<A, T, V> {
        override fun execute(operation: A, next: Link<A, *, T>?): Observable<V> {
            return firstLink.execute(operation, nextLink)
        }
    }
}

interface GraphQlLink<T, V> : Link<GraphQlOperation<*>, T, V>