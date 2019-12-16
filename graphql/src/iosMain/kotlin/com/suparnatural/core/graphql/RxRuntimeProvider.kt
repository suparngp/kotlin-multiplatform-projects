package com.suparnatural.core.graphql

import com.suparnatural.core.rx.ObservableFactory
import com.suparnatural.core.rx.PublishSubjectFactory
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
actual object RxRuntimeProvider {
    actual var observableFactory: ObservableFactory = EmptyObservableFactory()
    actual var publishSubjectFactory: PublishSubjectFactory = EmptyPublishSubjectFactory()
}