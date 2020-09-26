package com.suparnatural.core.utilities

/**
 * Returns the execution time of a [block] in milliseconds.
 */
actual fun measureTimeMillis(block: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - startTime
}

/**
 * Returns the execution time of a [block] in nano seconds.
 */
actual fun measureNanoTime(block: () -> Unit): Long {
    val startTime = System.nanoTime()
    block()
    return System.nanoTime() - startTime
}

/**
 * Returns the execution time of a [block] in micro seconds.
 */
actual fun measureTimeMicros(block: () -> Unit): Long {
    return measureNanoTime(block) / 1000
}