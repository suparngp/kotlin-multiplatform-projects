package com.suparnatural.core.cache

import com.suparnatural.core.threading.ImmutableValue

/**
 * A cache replacement policy which evicts the oldest object from cache
 * to make space when cache is full.
 */
class FifoCacheReplacementPolicy(override  val cacheSize: Int): CacheReplacementPolicy {
    private var listHeadUnsafe by ImmutableValue<CacheableContainer?>(null)
    private var listTailUnsafe by ImmutableValue<CacheableContainer?>(null)
    private var currentSize by ImmutableValue(0)

    override fun registerObjectUnsafe(obj: CacheableContainer): CacheableContainer? {


        val removed: CacheableContainer? = if(currentSize == cacheSize) evictUnsafe() else null
        val tail = listTailUnsafe
        if (tail == null) {
            listHeadUnsafe = obj
            listTailUnsafe = obj
        } else {
            listTailUnsafe = obj
            tail.next = obj
            obj.previous = tail
        }
        currentSize += 1
        return removed
    }

    override fun unregisterObjectUnsafe(obj: CacheableContainer) {
        val previous = obj.previous
        val next = obj.next
        previous?.next = next
        next?.previous = previous
    }

    override fun evictUnsafe(): CacheableContainer? {
        val head = listHeadUnsafe ?: return null
        val nextHead = head.next ?: return null
        head.next = null
        head.previous = null

        nextHead.previous = null
        listHeadUnsafe = nextHead
        currentSize -= 1

        return head
    }

    override fun clearRegistryUnsafe() {
        listHeadUnsafe = null
        listTailUnsafe = null
        currentSize = 0
    }
}