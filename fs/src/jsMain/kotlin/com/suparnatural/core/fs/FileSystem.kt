package com.suparnatural.core.fs

import fs.*
import ncp
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

    /** converts a [ContentEncoding] to an object compatible with the nodeJS generated fs options types */
    private fun encodingOptions(encoding: ContentEncoding) = object : `T$44`, `T$45` {
        override var encoding: String? = encoding.toString().toLowerCase()
        override var flag: String? = null
    }

    /** replaces `/` with `\` on windows because otherwise functions like [mkdir] don't work with recursive */
    internal fun fixPathString(path: String) =
        if (os.platform() == "win32") path.replace("/", "\\") else path

    /**
     * Returns a list of stats for the contents of directory at `path`.
     */
    actual fun readDir(directory: String): List<StatResult>? =
        readdirSync(fixPathString(directory), encodingOptions(ContentEncoding.Utf8)).map {
            stat(path.join(directory, it))!!
        }

    /**
     * Returns a list of stats for the contents of directory at `pathComponent`.
     */
    actual fun readDir(pathComponent: PathComponent): List<StatResult>? = pathComponent.component?.let { readDir(it) }

    /**
     * Returns stats for the resource at `path`.
     */
    actual fun stat(fullPath: String): StatResult? {
        val fixedPath = fixPathString(fullPath)
        val stat = statSync(fixedPath)
        return StatResult(
            name = path.basename(fixedPath),
            absolutePath = PathComponent(fixedPath),
            canonicalPath = PathComponent(fixedPath),
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
        readFileSync(fixPathString(path), encodingOptions(encoding)) as? String

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
        readFileSync(fixPathString(path), encodingOptions(ContentEncoding.Utf8)).toString().encodeToByteArray()

    /**
     * Returns the contents of the file located at `pathComponent` as ByteArray.
     */
    actual fun readFile(pathComponent: PathComponent): ByteArray? = pathComponent.component?.let { readFile(it) }

    /**
     * tries to execute the given [callback]
     * If [create] is true, then file is created if it does not exist.
     * @return true if operation is successful, otherwise false.
     */
    private fun tryIfExists(path: String, create: Boolean, callback: () -> Unit): Boolean =
        if (!create && !exists(fixPathString(path))) {
            false
        } else {
            try {
                callback()
                true
            } catch (error: Throwable) {
                throw error
            }
        }

    /**
     * Writes `contents` to the file located at `path`. If `create` is true, then file is created if it does not exist.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     * * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean =
        tryIfExists(fixPathString(path), create) { writeFileSync(path, contents, encodingOptions(encoding)) }

    /**
     * Writes `contents` to the file located at `pathComponent`.
     * If `create` is true, then file is created if it does not exist.
     * For binary files, use `encoding` = [ContentEncoding.Base64].
     * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(
        pathComponent: PathComponent,
        contents: String,
        create: Boolean,
        encoding: ContentEncoding
    ): Boolean =
        pathComponent.component?.let { writeFile(it, contents, create, encoding) } ?: false

    /**
     * Writes `contents` to the file located at `path`. If `create` is true, then file is created if it does not exist.
     * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(path: String, contents: ByteArray, create: Boolean): Boolean =
        writeFile(fixPathString(path), contents.toString(), create, ContentEncoding.Utf8)

    /**
     * Writes `contents` to the file located at `pathComponent`. If `create` is true, then file is created if it does not exist.
     * Returns true if operation is successful, otherwise false.
     */
    actual fun writeFile(pathComponent: PathComponent, contents: ByteArray, create: Boolean): Boolean =
        pathComponent.component?.let { writeFile(it, contents, create) } ?: false

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
    actual fun appendFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean =
        tryIfExists(fixPathString(path), create) { appendFileSync(path, contents, encodingOptions(encoding)) }

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
    actual fun appendFile(
        pathComponent: PathComponent,
        contents: String,
        create: Boolean,
        encoding: ContentEncoding
    ): Boolean =
        pathComponent.component?.let { appendFile(it, contents, create, encoding) } ?: false

    /**
     * Creates a file at `path` if it does not exist.
     * Returns false if file already exists, otherwise true.
     */
    actual fun touch(path: String): Boolean =
        if (exists(path))
            false
        else
            true.also { writeFileSync(fixPathString(path), "", encodingOptions(ContentEncoding.Utf8)) }

    /**
     * Creates a file at `pathComponent` if it does not exist.
     * Returns false if file already exists, otherwise true.
     */
    actual fun touch(pathComponent: PathComponent): Boolean = pathComponent.component?.let { touch(it) } ?: false

    /**
     * Creates a directory on `path`.
     * If `recursive` is true, then intermediate directories are also created.
     * Returns true if directory is created successfully.
     */
    @Suppress("UnsafeCastFromDynamic") //casting explicitly causes "illegal cast" exception
    actual fun mkdir(path: String, recursive: Boolean): Boolean = tryIfExists(path, true) {
        mkdirSync(fixPathString(path), object : MakeDirectoryOptions {
            override var recursive: Boolean? = recursive
        })
    }

    /**
     * Creates a directory on `pathComponent`.
     * If `recursive` is true, then intermediate directories are also created.
     * Returns true if directory is created successfully.
     */
    actual fun mkdir(pathComponent: PathComponent, recursive: Boolean): Boolean =
        pathComponent.component?.let { mkdir(it, recursive) } ?: false

    /**
     * Returns true if the file or directory exists at `path`.
     */
    actual fun exists(path: String): Boolean = existsSync(fixPathString(path))

    /**
     * Returns true if the file or directory exists at `pathComponent`.
     */
    actual fun exists(pathComponent: PathComponent): Boolean = pathComponent.component?.let { exists(it) } ?: false

    /**
     * Removes a file on `path`.
     * If it is a directory, its contents are removed as well.
     * Returns true if file is deleted successfully, otherwise false.
     */
    //cant create an object that extends RmDirOptions since the compiler changes the symbol name,
    // and JsName isn't allowed on override values:
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    actual fun unlink(path: String): Boolean =
        try {
            val fixedPath = fixPathString(path)
            when (stat(fixedPath)!!.type) {
                FileType.Regular -> unlinkSync(fixedPath)
                //cant create an object that implements RmDirOptions as the compiler changes the symbol name:
                FileType.Directory -> rmdirSync(fixedPath, js("{recursive: true}") as RmDirOptions)
                else -> error("unknown filetype")
            }
            true
        } catch (error: Throwable) {
            false
        }

    /**
     * Removes a file on `pathComponent`.
     * If it is a directory, its contents are removed as well.
     * Returns true if file is deleted successfully, otherwise false.
     */
    actual fun unlink(pathComponent: PathComponent): Boolean = pathComponent.component?.let { unlink(it) } ?: false

    /**
     * Moves the file from `srcPath` to `destPath`.
     * If `srcPath` is a directory, its contents including hidden files are moved.
     * Returns true if the move is successful, otherwise false.
     */
    actual fun moveFile(srcPath: String, destPath: String): Boolean =
        copyFile(fixPathString(srcPath), fixPathString(destPath)) && unlink(srcPath)

    /**
     * Moves the file from `srcPathComponent` to `destPathComponent`.
     * If `srcPathComponent` is a directory, its contents including hidden files are moved.
     * Returns true if the move is successful, otherwise false.
     */
    actual fun moveFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean =
        copyFile(srcPathComponent, destPathComponent) && unlink(srcPathComponent)

    /**
     * Copies the file from `srcPath` to `destPath`.
     * If `srcPath` is a directory, its contents including hidden files are copied.
     * Returns true if the copy is successful, otherwise false.
     */
    actual fun copyFile(srcPath: String, destPath: String): Boolean = try {
        val fixedSrc = fixPathString(srcPath)
        val fixedDest = fixPathString(destPath)
        when (stat(srcPath)!!.type) {
            FileType.Regular -> copyFileSync(fixedSrc, fixedDest)
            FileType.Directory -> ncp(fixedSrc, fixedDest) { err -> err?.forEach { error(it) } }
            else -> error("unknown filetype")
        }
        true
    } catch (error: Throwable) {
        false
    }

    /**
     * Copies the file from `srcPathComponent` to `destPathComponent`.
     * If `srcPathComponent` is a directory, its contents including hidden files are copied.
     * Returns true if the copy is successful, otherwise false.
     */
    actual fun copyFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean =
        if (srcPathComponent.component == null || destPathComponent.component == null)
            false
        else
            copyFile(srcPathComponent.component, destPathComponent.component)
}