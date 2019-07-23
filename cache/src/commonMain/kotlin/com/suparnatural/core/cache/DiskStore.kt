package com.suparnatural.core.cache

import com.suparnatural.core.fs.FileSystem
import com.suparnatural.core.fs.PathComponent
import com.suparnatural.core.threading.Future

/**
 * A [CacheStore] which persists [Cacheable] objects on the disk.
 * By default, the store blocks the calling thread for all its operations which can changed by
 * setting [blocking] to false.
 *
 * A new file is created under [location] per cached object.
 *
 * Note: Even though [DiskStore] does not impose any limits on the number of persisted objects,
 * the underlying platform may impose a limit on number of files created by the application
 * in which case the persistence will fail without any recovery.
 */
class DiskStore constructor(
        override val blocking: Boolean = true,
        override val preprocessors: List<CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>>? = null,
        /**
         * Location of cache directory on the disk.
         * Defaults to DocumentsDirectory/cache.
         */
        var location: PathComponent = FileSystem.contentsDirectory.absolutePath?.byAppending("cache")!!
) : CacheStore {



    /**
     * [CacheStoreWorker] depending upon [blocking].
     */
    private val worker: CacheStoreWorker = if (blocking) CacheStoreBlockingWorker() else CacheStoreNonBlockingWorker()

    init {
        FileSystem.mkdir(location, true)
    }

    override fun <T : Cacheable> persistObject(obj: T) {
        val key = obj.cacheKey()
        val cacheFilePath = location.byAppending(key) ?: return
        val input = Triple(obj as Cacheable, cacheFilePath, preprocessors)
        worker.persistObject(input) {
            val processed = it.third?.fold(it.first) { prev, preprocessor ->
                (preprocessor).archive(prev)
            } ?: it.first
            val content = processed.serializeForPersistence()
            FileSystem.writeFile(it.second, content, true)
        }
    }

    override fun unlinkObject(key: String) {
        val cacheFilePath = location.byAppending(key) ?: return
        worker.unlinkObject(cacheFilePath) {
            FileSystem.unlink(it)
        }
    }

    override fun flushAndClose(): Future<Unit> {
        return worker.terminate()
    }

    override fun fetchAllObjects(): List<Cacheable> {
        if (preprocessors == null || preprocessors.isEmpty()) return emptyList()
        val allObjects = FileSystem.readDir(location) ?: return emptyList()
        return allObjects.mapNotNull {
            val path = it.canonicalPath.component
            if (path == null)
                null
            else
                Pair(it.name, path)
        }.mapNotNull {
            val contents = FileSystem.readFile(it.second)
            if (contents == null || contents.isEmpty())
                null
            else RawCacheable(it.first, contents)
        }.map {
            preprocessors.foldRight(it as Cacheable) { preprocessor, o ->
                preprocessor.unarchive(o)
            }
        }
    }

    override fun wipe() {
        val allObjects = FileSystem.readDir(location) ?: return
        allObjects.forEach {
            FileSystem.unlink(it.canonicalPath)
        }
    }
}
