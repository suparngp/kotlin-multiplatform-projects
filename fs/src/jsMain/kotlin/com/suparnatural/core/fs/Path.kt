package com.suparnatural.core.fs

import child_process.`T$16`
import path.path

/**
 * Represents a file system path to a resource
 */
actual class Path actual constructor() {

    constructor(absolutePath: String?, relativePath: String?) : this() {
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
    private object realpathOptions: `T$16` {
        override var encoding: String? = "utf8"
    }
    private val canonicalPath = if (component != null) fs.realpathSync(component, realpathOptions) as String else null

    override fun toString(): String {
        return "[component=$component, canonicalPath=$canonicalPath]"
    }

    /**
     * Create a new [PathComponent] by appending [component] string.
     */
    actual fun byAppending(component: String): PathComponent? {
        if (canonicalPath == null) return null
        return PathComponent(path.join(component, canonicalPath))
    }

}