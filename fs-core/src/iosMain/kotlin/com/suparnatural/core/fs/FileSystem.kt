package com.suparnatural.core.fs

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.posix.pathconf
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

val String.nativeStr: NSString
    get() = NSString.create(string = this)

fun String.toData(encoding: ContentEncoding): NSData? {
    return when (encoding) {
        ContentEncoding.Base64 -> nativeStr.dataUsingEncoding(NSUTF8StringEncoding)
                ?.base64EncodedDataWithOptions(NSDataBase64EncodingEndLineWithCarriageReturn)
        ContentEncoding.Ascii -> nativeStr.dataUsingEncoding(NSASCIIStringEncoding)
        ContentEncoding.Utf8 -> nativeStr.dataUsingEncoding(NSUTF8StringEncoding)
    }
}

fun String.Companion.fromData(data: NSData, encoding: ContentEncoding): String? {
    return when (encoding) {
        ContentEncoding.Utf8 -> NSString.create(data, NSUTF8StringEncoding).toString()
        ContentEncoding.Ascii -> NSString.create(data, NSASCIIStringEncoding).toString()
        ContentEncoding.Base64 -> {
            val base64String = NSString.create(data = data, encoding = NSUTF8StringEncoding)?.toString() ?: return null

            return base64String.split("\n")
                    .filter { it.isNotEmpty() }
                    .mapNotNull {
                        NSData.create(it, NSDataBase64EncodingEndLineWithCarriageReturn)
                    }.mapNotNull {
                        String.fromData(it, ContentEncoding.Utf8)
                    }.joinToString("\n")
        }

    }
}


actual object FileSystem {

    private var manager = NSFileManager.defaultManager

    private fun getDirUrl(directory: NSSearchPathDirectory, create: Boolean = false): NSURL? {
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            return manager.URLForDirectory(directory, NSUserDomainMask, null, create, error.ptr)?.standardizedURL
        }
    }

    private fun getDirPath(directory: NSSearchPathDirectory, create: Boolean = false): Path {
        return Path.fromUrl(getDirUrl(directory, create))
    }

    private fun attributesOfFile(filePath: String): Map<String, Any?> {
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            val attributes = manager.attributesOfItemAtPath(filePath, error.ptr) ?: return emptyMap()
            val result = mutableMapOf<String, Any?>()
            for ((key, value) in attributes) {
                if (key != null && key is String) {
                    result[key] = value
                }
            }
            return result
        }
    }

    actual val contentsDirectory: Path
        get() = getDirPath(NSDocumentDirectory, true)

    actual val cachesDirectory: Path
        get() = getDirPath(NSCachesDirectory, true)

    actual val temporaryDirectory: Path
        get() = Path.fromUrlString(NSTemporaryDirectory())

    private fun fileExists(path: String, isDirectory: Boolean = false): Boolean {
        memScoped {
            val boolean = alloc<BooleanVar>()
            boolean.value = isDirectory
            return manager.fileExistsAtPath(path, isDirectory = boolean.ptr)
        }
    }

    private fun ensureFileExists(path: String, create: Boolean = false, isDirectory: Boolean = false): Boolean {
        val exists = fileExists(path, isDirectory)
        if (!exists && !create) return false

        if (!exists && create) {
            return if (isDirectory) {
                manager.createDirectoryAtPath(path, emptyMap<Any?, Any>())
            } else {
                manager.createFileAtPath(path, null, emptyMap<Any?, Any>())
            }
        }
        return true
    }

    private fun readDir(url: NSURL?): List<StatResult>? {
        val stdPath = url?.standardizedURL?.path ?: return null
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            val contents = manager.contentsOfDirectoryAtPath(stdPath, error.ptr) ?: return emptyList()
            return contents.mapNotNull {
                stat(url.URLByAppendingPathComponent(it as String) ?: return null)
            }
        }
    }

    actual fun readDir(path: String): List<StatResult>? = readDir(Path.urlFromString(path))
    actual fun readDir(pathComponent: PathComponent): List<StatResult>? = readDir(pathComponent.url)


    private fun stat(url: NSURL?): StatResult? {
        val stdPath = url?.standardizedURL?.path ?: return null
        val attributes = attributesOfFile(stdPath)
        val createdAt = toInterval(attributes[NSFileCreationDate])
        val modifiedAt = toInterval(attributes[NSFileModificationDate])
        val size = attributes[NSFileSize] as? Double
        val type = {
            when (attributes[NSFileType] as? String) {
                NSFileTypeRegular -> FileType.Regular
                NSFileTypeDirectory -> FileType.Directory
                else -> FileType.Unknown
            }
        }()

        return StatResult(
                name = url.lastPathComponent ?: "",
                canonicalPath = PathComponent(stdPath),
                absolutePath = PathComponent(stdPath),
                createdAt = createdAt,
                modifiedAt = modifiedAt,
                size = size,
                type = type)
    }

    actual fun stat(path: String): StatResult? = stat(Path.urlFromString(path))
    actual fun stat(pathComponent: PathComponent): StatResult? = stat(pathComponent.url)


    private fun readFile(url: NSURL?, encoding: ContentEncoding): String? {
        val path = url?.standardizedURL?.path ?: return null
        val data = manager.contentsAtPath(path) ?: return null
        return String.fromData(data, encoding)

    }

    actual fun readFileAsByteArray(path: String): ByteArray? {
        val url = Path.urlFromString(path)
        val path = url?.standardizedURL?.path ?: return null
        val data = manager.contentsAtPath(path) ?: return null
        return nsDataToByteArray(data)

    }

    private fun nsDataToByteArray(data: NSData): ByteArray? {
        if (data.bytes == null) {
            return null
        }
        return data.bytes!!.readBytes(data.length.toInt())
    }

    private fun byteArrayToNsData(byteArray: ByteArray): NSData? {
        if (byteArray.isEmpty()){
            return null
        }

        return byteArray.usePinned {
            return@usePinned NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong())
        }
    }

    actual fun readFile(path: String, encoding: ContentEncoding): String? = readFile(Path.urlFromString(path), encoding)
    actual fun readFile(pathComponent: PathComponent, encoding: ContentEncoding): String? = readFile(pathComponent.url, encoding)


    private fun toInterval(input: Any?): Double? {
        if (input == null || input !is NSDate) return null
        return input.timeIntervalSince1970()
    }

    actual fun writeFile(path: String, contents: ByteArray, create: Boolean): Boolean {
        val pathStandardized = Path.urlFromString(path)?.standardizedURL?.path ?: return false

        if (!ensureFileExists(pathStandardized, create, false)) {
            return false
        }

        val data = byteArrayToNsData(contents) ?: return false
        return data.writeToFile(pathStandardized, true)
    }

    private fun writeFile(url: NSURL?, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        val path = url?.standardizedURL?.path ?: return false

        if (!ensureFileExists(path, create, false))
            return false

        val data = contents.toData(encoding) ?: return false
        return data.writeToFile(path, true)
    }

    actual fun writeFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean = writeFile(Path.urlFromString(path), contents, create, encoding)
    actual fun writeFile(pathComponent: PathComponent, contents: String, create: Boolean, encoding: ContentEncoding): Boolean = writeFile(pathComponent.url, contents, create, encoding)

    private fun appendFile(url: NSURL?, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        val path = url?.standardizedURL?.path ?: return false

        if (encoding == ContentEncoding.Base64) {
            val existingContents =  readFile(url, ContentEncoding.Base64) ?: ""
            return writeFile(url, (existingContents + contents), create=create, encoding=ContentEncoding.Base64)
        }

        val data = contents.toData(encoding) ?: return false
        if (!ensureFileExists(path, create, false)) return false

        val handle = NSFileHandle.fileHandleForUpdatingAtPath(path) ?: return false
        handle.seekToEndOfFile()
        handle.writeData(data)
        handle.closeFile()
        return true
    }

    actual fun appendFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean = appendFile(Path.urlFromString(path), contents, create, encoding)
    actual fun appendFile(pathComponent: PathComponent, contents: String, create: Boolean, encoding: ContentEncoding): Boolean = appendFile(pathComponent.url, contents, create, encoding)

    private fun touch(url: NSURL?): Boolean {
        val path = url?.standardizedURL?.path ?: return false
        if (fileExists(path, false)) return false
        return manager.createFileAtPath(path, null, null)
    }

    actual fun touch(path: String): Boolean = touch(Path.urlFromString(path))
    actual fun touch(pathComponent: PathComponent): Boolean = touch(pathComponent.url)

    private fun mkdir(url: NSURL?, recursive: Boolean): Boolean {

        val path = url?.standardizedURL?.path ?: return false
        if (ensureFileExists(path, create = false, isDirectory = true)) return false
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            return manager.createDirectoryAtPath(path, recursive, emptyMap<Any?, Any>(), error.ptr)
        }
    }

    actual fun mkdir(path: String, recursive: Boolean): Boolean = mkdir(Path.urlFromString(path), recursive)
    actual fun mkdir(pathComponent: PathComponent, recursive: Boolean): Boolean = mkdir(pathComponent.url, recursive)

    private fun exists(url: NSURL?): Boolean {
        val path = url?.standardizedURL?.path ?: return false
        return manager.fileExistsAtPath(path)
    }

    actual fun exists(path: String): Boolean = exists(Path.urlFromString(path))
    actual fun exists(pathComponent: PathComponent): Boolean = exists(pathComponent.url)

    private fun unlink(url: NSURL?): Boolean {
        val path = url?.standardizedURL?.path ?: return false
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            return manager.removeItemAtPath(path, error.ptr)
        }
    }

    actual fun unlink(path: String): Boolean = unlink(Path.urlFromString(path))
    actual fun unlink(pathComponent: PathComponent): Boolean = unlink(pathComponent.url)

    private fun moveFile(srcUrl: NSURL?, destUrl: NSURL?): Boolean {
        val srcPath = srcUrl?.standardizedURL?.path ?: return false
        val destPath = destUrl?.standardizedURL?.path ?: return false
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            return manager.moveItemAtPath(srcPath, destPath, error.ptr)
        }
    }

    actual fun moveFile(srcPath: String, destPath: String): Boolean = moveFile(Path.urlFromString(srcPath), Path.urlFromString(destPath))
    actual fun moveFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean = moveFile(srcPathComponent.url, destPathComponent.url)


    private fun copyFile(srcUrl: NSURL?, destUrl: NSURL?): Boolean {
        val srcPath = srcUrl?.standardizedURL?.path ?: return false
        val destPath = destUrl?.standardizedURL?.path ?: return false
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            return manager.copyItemAtPath(srcPath, destPath, error.ptr)
        }
    }

    actual fun copyFile(srcPath: String, destPath: String): Boolean = copyFile(Path.urlFromString(srcPath), Path.urlFromString(destPath))
    actual fun copyFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean = copyFile(srcPathComponent.url, destPathComponent.url)


}