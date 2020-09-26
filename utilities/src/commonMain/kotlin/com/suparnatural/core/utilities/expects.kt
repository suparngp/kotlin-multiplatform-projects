package com.suparnatural.core.utilities

/**
 * Returns the execution time of a [block] in milliseconds.
 */
expect fun measureTimeMillis(block: () -> Unit): Long

/**
 * Returns the execution time of a [block] in nano seconds.
 */
expect fun measureNanoTime(block: () -> Unit): Long

/**
 * Returns the execution time of a [block] in micro seconds.
 */
expect fun measureTimeMicros(block: () -> Unit): Long