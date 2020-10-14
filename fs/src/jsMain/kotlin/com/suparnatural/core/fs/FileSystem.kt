package com.suparnatural.core.fs

import fs.`T$44`
import fs.readdirSync
import path.path

/**
 * A thread safe singleton to access file system apis.
 *
 * ### Examples
 *
 * ```
 * val documentsDirectory = FileSystem.contentsDirectory
 *
 * val isFileCreated = FileSystem.touch(documentsDirectory.absolutePath?.byAppending("test.txt")!!)
 * ```
 */
actual object FileSystem {
    actual val contentsDirectory: Path = Path(null, null)
    actual val cachesDirectory: Path = Path(null, null)
    actual val temporaryDirectory: Path = Path(null, null)

    /** converts a [ContentEncoding] to an object compatible with the nodeJS generated fs options type */
    private fun encodingOptions(encoding: ContentEncoding) = object : `T$44` {
        override var encoding: String? = encoding.toString().toLowerCase()
    }

    /**
     * Returns a list of stats for the contents of directory at `path`.
     */
    actual fun readDir(path: String): List<StatResult>? = readdirSync(path).map { stat(it)!! }

    /**
     * Returns a list of stats for the contents of directory at `pathComponent`.
     */
    actual fun readDir(pathComponent: PathComponent): List<StatResult>? = pathComponent.component?.let { readDir(it) }

    /**
     * Returns stats for the resource at `path`.
     */
    actual fun stat(fullPath: String): StatResult? {
        val stat = fs.statSync(fullPath)
        return StatResult(
                name = path.basename(fullPath),
                absolutePath = PathComponent(fullPath),
                canonicalPath = PathComponent(fullPath),
                createdAt = stat.birthtime.getTime(),
                size = stat.size.toDouble(),
                type = {
                    when (true) {
                        stat.isFile() -> FileType.Regular
                        stat.isDirectory() -> FileType.Directory
                        else -> FileType.Unknown
                    }
                }()
        )
    }

    /**
     * Returns stats for the resource at `pathComponent`.
     */
    actual fun stat(pathComponent: PathComponent): StatResult? = pathComponent.component?.let { stat(it) }

    /**
     *
     * Returns the contents of the file located at `path`. The content is parsed according to `encoding`.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     *
     */
    actual fun readFile(path: String, encoding: ContentEncoding): String? =
            fs.readFileSync(path, encodingOptions(encoding)) as? String

    /**
     *
     * Returns the contents of the file located at `pathComponent`. The content is parsed according to `encoding`.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     *
     */
    actual fun readFile(pathComponent: PathComponent, encoding: ContentEncoding): String? =
            pathComponent.component?.let { readFile(it, encoding) }

    /**
     * Returns the contents of the file located at `path` as ByteArray.
     */
    actual fun readFile(path: String): ByteArray? =
            fs.readFileSync(path, encodingOptions(ContentEncoding.Utf8)).toString().encodeToByteArray()

    /**
     * Returns the contents of the file located at `pathComponent` as ByteArray.
     */
    actual fun readFile(pathComponent: PathComponent): ByteArray? = pathComponent.component?.let { readFile(it) }

    /**
     * Writes `contents` to the file located at `path`. If `create` is true, then file is created if it does not exist.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     * * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Writes `contents` to the file located at `pathComponent`.
     * If `create` is true, then file is created if it does not exist.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(pathComponent: PathComponent, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Writes `contents` to the file located at `path`. If `create` is true, then file is created if it does not exist.
     * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(path: String, contents: ByteArray, create: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Writes `contents` to the file located at `pathComponent`. If `create` is true, then file is created if it does not exist.
     * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(pathComponent: PathComponent, contents: ByteArray, create: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Appends `contents` to the file located at `path`. If `create` is true, then file is created if it does not exist.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     * The original contents are left unchanged.
     *
     * **If the `encoding` = [ContentEncoding.Base64], then existing contents are first read decoded into a UTF-8 string.
     * The new contents are appended and a new Base64 representation of the concatenated string is written to the file.**
     *
     * Returns true if operation is successful, otherwise false.
     */
    actual fun appendFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Appends `contents` to the file located at `pathComponent`.
     * If `create` is true, then file is created if it does not exist.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     * The original contents are left unchanged.
     *
     * **If the `encoding` = [ContentEncoding.Base64], then existing contents are first read decoded into a UTF-8 string.
     * The new contents are appended and a new Base64 representation of the concatenated string is written to the file.**
     *
     * Returns true if operation is successful, otherwise false.
     */
    actual fun appendFile(pathComponent: PathComponent, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Creates a file at `path` if it does not exist.
     * Returns false if file already exists, otherwise true.
     */
    actual fun touch(path: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Creates a file at `pathComponent` if it does not exist.
     * Returns false if file already exists, otherwise true.
     */
    actual fun touch(pathComponent: PathComponent): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Creates a directory on `path`.
     * If `recursive` is true, then intermediate directories are also created.
     * Returns true if directory is created successfully.
     */
    actual fun mkdir(path: String, recursive: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Creates a directory on `pathComponent`.
     * If `recursive` is true, then intermediate directories are also created.
     * Returns true if directory is created successfully.
     */
    actual fun mkdir(pathComponent: PathComponent, recursive: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Returns true if the file or directory exists at `path`.
     */
    actual fun exists(path: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Returns true if the file or directory exists at `pathComponent`.
     */
    actual fun exists(pathComponent: PathComponent): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Removes a file on `path`.
     * If it is a directory, its contents are removed as well.
     * Returns true if file is deleted successfully, otherwise false.
     */
    actual fun unlink(path: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Removes a file on `pathComponent`.
     * If it is a directory, its contents are removed as well.
     * Returns true if file is deleted successfully, otherwise false.
     */
    actual fun unlink(pathComponent: PathComponent): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Moves the file from `srcPath` to `destPath`.
     * If `srcPath` is a directory, its contents including hidden files are moved.
     * Returns true if the move is successful, otherwise false.
     */
    actual fun moveFile(srcPath: String, destPath: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Moves the file from `srcPathComponent` to `destPathComponent`.
     * If `srcPathComponent` is a directory, its contents including hidden files are moved.
     * Returns true if the move is successful, otherwise false.
     */
    actual fun moveFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Copies the file from `srcPath` to `destPath`.
     * If `srcPath` is a directory, its contents including hidden files are copied.
     * Returns true if the copy is successful, otherwise false.
     */
    actual fun copyFile(srcPath: String, destPath: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Copies the file from `srcPathComponent` to `destPathComponent`.
     * If `srcPathComponent` is a directory, its contents including hidden files are copied.
     * Returns true if the copy is successful, otherwise false.
     */
    actual fun copyFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean {
        TODO("Not yet implemented")
    }


}