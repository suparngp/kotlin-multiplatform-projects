package com.suparnatural.core.utilities

actual fun measureTimeMillis(block: () -> Unit): Long = kotlin.system.measureTimeMillis(block)
actual fun measureNanoTime(block: () -> Unit): Long = kotlin.system.measureNanoTime(block)
actual fun measureTimeMicros(block: () -> Unit): Long = kotlin.system.measureTimeMicros(block)