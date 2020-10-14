package com.suparnatural.core.fs

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
    /**
     * Path to the common contents directory. Directory is guaranteed to exist (created if not exists already).
     *
     * On Android, it points to `files` directory.
     * On iOS, it points to `Documents` directory.
     */
    actual val contentsDirectory: Path
        get() = TODO("Not yet implemented")

    /**
     * Path to caches directory. Directory is guaranteed to exist (created if not exists already).
     */
    actual val cachesDirectory: Path
        get() = TODO("Not yet implemented")

    /**
     * Path to Temporary directory. Directory is guaranteed to exist (created if not exists already).
     */
    actual val temporaryDirectory: Path
        get() = TODO("Not yet implemented")

    /**
     * Returns a list of stats for the contents of directory at `path`.
     */
    actual fun readDir(path: String): List<StatResult>? {
        TODO("Not yet implemented")
    }

    /**
     * Returns a list of stats for the contents of directory at `pathComponent`.
     */
    actual fun readDir(pathComponent: PathComponent): List<StatResult>? {
        TODO("Not yet implemented")
    }

    /**
     * Returns stats for the resource at `path`.
     */
    actual fun stat(path: String): StatResult? {
        TODO("Not yet implemented")
    }

    /**
     * Returns stats for the resource at `pathComponent`.
     */
    actual fun stat(pathComponent: PathComponent): StatResult? {
        TODO("Not yet implemented")
    }

    /**
     *
     * Returns the contents of the file located at `path`. The content is parsed according to `encoding`.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     *
     */
    actual fun readFile(path: String, encoding: ContentEncoding): String? {
        TODO("Not yet implemented")
    }

    /**
     *
     * Returns the contents of the file located at `pathComponent`. The content is parsed according to `encoding`.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     *
     */
    actual fun readFile(pathComponent: PathComponent, encoding: ContentEncoding): String? {
        TODO("Not yet implemented")
    }

    /**
     * Returns the contents of the file located at `path` as ByteArray.
     */
    actual fun readFile(path: String): ByteArray? {
        TODO("Not yet implemented")
    }

    /**
     * Returns the contents of the file located at `pathComponent` as ByteArray.
     */
    actual fun readFile(pathComponent: PathComponent): ByteArray? {
        TODO("Not yet implemented")
    }

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