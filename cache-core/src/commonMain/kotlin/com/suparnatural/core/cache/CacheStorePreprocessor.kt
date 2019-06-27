package com.suparnatural.core.cache

/**
 * A [CacheStorePreprocessor] processes [Cacheable] objects before persisting them in the store
 * and after they are read from the store.
 *
 * For example, a preprocessor can encrypt a [Cacheable] object before it is persisted to the [CacheStore]
 * and decrypt the persisted value when it is read from the [CacheStore].
 *
 * It takes three parameters [T], [R] and [V] which are all [Cacheable].
 * The preprocessor archives [T] and transforms it into [R] which can then be archived
 * by the next preprocessor. Similarly, it unarchives [V] into a [T].
 *
 * In many cases, [R] and [V] can be same. However, a [CacheStore] performs a final archiving of the object
 * in a [RawCacheable] object before persisting it. Therefore for the last preprocessor in the chain,
 * [V] can be [RawCacheable] while [R] can be another type.
 *
 * ### Examples
 *
 * #### An encrypted Preprocessor
 *
 * ```
 * data class PlainObject(val plainText: String): Cacheable....
 * data class EncryptedObject(val cipherText: String): Cacheable...
 *
 * class SecureStorePreprocessor: CacheStorePreprocessor<PlainObject, EncryptedObject, RawCacheable> {
 *     override fun archive(obj: PlainObject) {
 *         return EncryptedObject(cipherText = aes256.encrypt(KEY, obj.plainText))
 *     }
 *
 *     override fun unarchive(obj: RawCacheable): Person {
 *         val encryptedObject = EncryptedObject.deserializeFromPersistence(obj.key, obj.value)
 *         return Person(plainText = aes256.decrypt(KEY, encryptedObject.cipherText)
 *     }
 * }
 *
 * ```
 *
 */
interface CacheStorePreprocessor<T: Cacheable, out R: Cacheable, in V: Cacheable> {

    /**
     * Archives the `T` into `R`
     */
    fun archive(obj: T): R

    /**
     * Unarchives the `V` into `T`
     */
    fun unarchive(obj: V): T
}
