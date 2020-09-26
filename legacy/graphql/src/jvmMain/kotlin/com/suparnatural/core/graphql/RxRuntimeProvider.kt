package com.suparnatural.core.graphql

import com.suparnatural.core.rx.ObservableFactory
import com.suparnatural.core.rx.PublishSubjectFactory

actual object RxRuntimeProvider {
    actual var observableFactory: ObservableFactory = EmptyObservableFactory()
    actual var publishSubjectFactory: PublishSubjectFactory = EmptyPublishSubjectFactory()
}