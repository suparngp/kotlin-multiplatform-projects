package com.suparnatural.core.fs

import java.io.*
import java.util.*

actual object FileSystem {

    actual val contentsDirectory: Path = Path(null, null)

    actual val cachesDirectory: Path = Path(null, null)

    actual val temporaryDirectory: Path = Path(null, null)

    private fun buildStats(file: File): StatResult {
        val fileType = {
            when {
                file.isDirectory -> FileType.Directory
                file.isFile -> FileType.Regular
                else -> FileType.Unknown
            }
        }()
        return StatResult(
                name = file.name,
                absolutePath = PathComponent(file.absolutePath),
                canonicalPath = PathComponent(file.canonicalPath),
                createdAt = 0.0,
                modifiedAt = file.lastModified().toDouble(),
                size = file.length().toDouble(),
                type = fileType
        )
    }

    actual fun readDir(path: String): List<StatResult>? {
        val parent = File(path).canonicalFile ?: return null
        val fileList = parent.listFiles() ?: return null
        return fileList.map {
            buildStats(it)
        }
    }


    actual fun readDir(pathComponent: PathComponent): List<StatResult>? {
        val path = pathComponent.component ?: return null
        return readDir(path)
    }


    actual fun stat(path: String): StatResult? {
        val file = File(path)
        return buildStats(file)
    }


    actual fun stat(pathComponent: PathComponent): StatResult? {
        val path = pathComponent.component ?: return null
        return stat(path)
    }


    actual fun readFile(path: String, encoding: ContentEncoding): String? {
        val file = File(path).canonicalFile

        val charset = {
            when (encoding) {
                ContentEncoding.Ascii -> Charsets.US_ASCII
                else -> Charsets.UTF_8
            }
        }()
        val reader = BufferedReader(InputStreamReader(FileInputStream(file), charset))
        val content = reader.readLines().joinToString("\n")

        if (encoding == ContentEncoding.Base64) {
            return String(Base64.getDecoder().decode(content), Charsets.UTF_8)
        }
        return content
    }


    actual fun readFile(pathComponent: PathComponent, encoding: ContentEncoding): String? {
        val path = pathComponent.component ?: return null
        return readFile(path, encoding)
    }

    private fun writeFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding, append: Boolean = false): Boolean {
        val file = File(path).canonicalFile
        if (!file.exists()) {
            if (!create) return false
            file.createNewFile()
        }

        val finalContent: String
        val appendToFile: Boolean

        if (encoding == ContentEncoding.Base64) {
            val sourceString = if (append) (readFile(path, ContentEncoding.Base64) ?: "") + contents else contents

            finalContent = Base64.getEncoder().encodeToString(sourceString.toByteArray(Charsets.UTF_8))
            appendToFile = false

        } else {
            finalContent = contents
            appendToFile = append
        }

        val charset = {
            when (encoding) {
                ContentEncoding.Ascii -> Charsets.US_ASCII
                else -> Charsets.UTF_8
            }
        }()
        val bufferedWriter = BufferedWriter(OutputStreamWriter(FileOutputStream(file, appendToFile), charset))
        bufferedWriter.write(finalContent)
        bufferedWriter.close()
        return true
    }


    actual fun writeFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        return writeFile(path, contents, create, encoding, false)
    }


    actual fun writeFile(pathComponent: PathComponent, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        val path = pathComponent.component ?: return false
        return writeFile(path, contents, create, encoding, false)
    }


    actual fun appendFile(path: String, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        return writeFile(path, contents, create, encoding, true)
    }


    actual fun appendFile(pathComponent: PathComponent, contents: String, create: Boolean, encoding: ContentEncoding): Boolean {
        val path = pathComponent.component ?: return false
        return writeFile(path, contents, create, encoding, true)
    }


    actual fun touch(path: String): Boolean {
        val file = File(path).canonicalFile
        return file.createNewFile()
    }


    actual fun touch(pathComponent: PathComponent): Boolean {
        val path = pathComponent.component ?: return false
        return touch(path)
    }


    actual fun mkdir(path: String, recursive: Boolean): Boolean {
        val file = File(path).canonicalFile
        return if (recursive) file.mkdirs() else file.mkdir()
    }


    actual fun mkdir(pathComponent: PathComponent, recursive: Boolean): Boolean {
        val path = pathComponent.component ?: return false
        return mkdir(path, recursive)
    }


    actual fun exists(path: String): Boolean {
        return File(path).canonicalFile.exists()
    }


    actual fun exists(pathComponent: PathComponent): Boolean {
        val path = pathComponent.component ?: return false
        return exists(path)
    }


    actual fun unlink(path: String): Boolean {
        return File(path).canonicalFile.deleteRecursively()
    }


    actual fun unlink(pathComponent: PathComponent): Boolean {
        val path = pathComponent.component ?: return false
        return unlink(path)
    }


    actual fun moveFile(srcPath: String, destPath: String): Boolean {
        val srcFile = File(srcPath).canonicalFile
        val destFile = File(destPath).canonicalFile
        return srcFile.renameTo(destFile)
    }


    actual fun moveFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean {
        val srcPath = srcPathComponent.component ?: return false
        val destPath = destPathComponent.component ?: return false
        return moveFile(srcPath, destPath)
    }


    actual fun copyFile(srcPath: String, destPath: String): Boolean {
        val srcFile = File(srcPath).canonicalFile
        val destFile = File(destPath).canonicalFile
        return srcFile.copyRecursively(destFile, true)
    }


    actual fun copyFile(srcPathComponent: PathComponent, destPathComponent: PathComponent): Boolean {
        val srcPath = srcPathComponent.component ?: return false
        val destPath = destPathComponent.component ?: return false
        return copyFile(srcPath, destPath)
    }

}