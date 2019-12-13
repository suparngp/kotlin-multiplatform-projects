package com.suparnatural.core.graphql

import com.suparnatural.core.rx.Observable

/**
 * A link takes an operation and returns an observable.
 * Links are a way to compose subsets of actions to handle
 * data. Links can be concatenated with each other to form
 * a chain to carry out an even complex data handling workflow.
 *
 * A link has only one method [execute] which accepts an operation of
 * type `A` and the next link in the chain. The next link can be null if
 * the current link is a terminating link. For example, the last link
 * can start a network request and return its response as an observable.
 *
 * To guarantee type safety, a Link has two type parameters `T` and `V`.
 * A link always returns an observable which emits a value of type `V`.
 * It can accept a link as the next link in the chain if and only if
 * the next link produces the observable of type `T`. In other words,
 * a link subscribe to an observable of type `T`, maps the values
 * into type `V` and returns an observable of those mapped values.
 *
 * @link https://www.apollographql.com/docs/link/overview/
 */
interface Link<A, T, V> {
    fun execute(operation: A, next: Link<A, *, T>? = null): Observable<V>
}


/**
 * Concatenates a link with another to form a chain
 */
fun <A, T, V> Link<A, T, V>.concat(nextLink: Link<A, *, T>): Link<A, T, V> {
    val firstLink = this;
    return object : Link<A, T, V> {
        override fun execute(operation: A, next: Link<A, *, T>?): Observable<V> {
            return firstLink.execute(operation, nextLink)
        }
    }
}

/**
 * Splits the link chain into two branches left and right based on a test.
 * If the test returns true, leftLink chain is followed, otherwise right link chain is followed.
 */
fun <A, T, V> Link<A, T, V>.split(test: (A) -> Boolean, leftLink: Link<A, *, T>, rightLink: Link<A, *, T>): Link<A, T, V> {
    val firstLink = this;
    return object : Link<A, T, V> {
        override fun execute(operation: A, next: Link<A, *, T>?): Observable<V> {
            val nextLink = if (test(operation)) leftLink else rightLink
            return firstLink.execute(operation, nextLink)
        }
    }
}


/**
 * A link which accepts [GraphQlOperation] as operations.
 */
interface GraphQlLink<T, V> : Link<GraphQlOperation<*>, T, V>