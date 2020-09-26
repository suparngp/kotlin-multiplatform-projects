package com.suparnatural.core.fs

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes

@OptIn(ExperimentalUnsignedTypes::class)
fun NSData.toByteArray(): ByteArray? {
    if (bytes == null) {
        return null
    }
    return bytes!!.readBytes(length.toInt())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun ByteArray.toNSData(): NSData? {
    if (isEmpty()) {
        return null
    }

    return usePinned {
        return@usePinned NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong())
    }
}