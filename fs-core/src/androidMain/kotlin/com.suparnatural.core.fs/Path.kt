package com.suparnatural.core.fs

import android.net.Uri
import java.io.File
import java.nio.file.Paths

/**
 * Represents a file system path to a resource
 */
actual class Path actual constructor() {

    constructor(absolutePath: String?, relativePath: String?): this() {
        this.absolutePath = PathComponent(absolutePath)
        this.relativePath = PathComponent(relativePath)
    }

    /**
     * Absolute path to the resource
     */
    actual var absolutePath: PathComponent? = null
    /**
     * Relative Path to the resource
     */
    actual var relativePath: PathComponent? = null

    actual companion object {

        val Empty = Path()
        fun urlFromString(urlString: String?) = if (urlString != null) Uri.fromFile(File(urlString)) else null
        fun fromUrl(url: Uri?) = Path(url?.path, url?.path)
        fun fromUrlString(urlString: String) = fromUrl(urlFromString(urlString))

        /**
         * Creates a new [Path] with the given [path] argument.
         * [path] is copied to both absolute and relative path components.
         */
        actual fun simplified(path: String): Path = Path(path, path)
    }

}

/**
 * A [Path] is made up of [PathComponent]s. For example, a path has [Path.absolutePath] and [Path.relativePath] which are both [PathComponent]s.
 */
actual class PathComponent actual constructor(actual val component: String?) {
    private val file = if (component != null) File(component).canonicalFile else null

    override fun toString(): String {
        return "[component=$component, canonicalPath=${file?.canonicalPath}, absolutePath=${file?.absolutePath}]"
    }

    /**
     * Create a new [PathComponent] by appending [component] string.
     */
    actual fun byAppending(component: String): PathComponent? {
        if (file == null) return null
        return PathComponent(File(file.absolutePath, component).canonicalFile.absolutePath)
    }

}