package com.suparnatural.core.fs

/**
 * A [Path] is made up of [PathComponent]s. For example, a path has [Path.absolutePath] and [Path.relativePath] which are both [PathComponent]s.
 */
expect class PathComponent(component: String?) {

    /**
     * Path used to create the component.
     */
    val component: String?

    /**
     * Create a new [PathComponent] by appending [component] string.
     */
    fun byAppending(component: String): PathComponent?
}

/**
 * Represents a file system path to a resource
 */
expect class Path() {
    /**
     * Absolute path to the resource
     */
    var absolutePath: PathComponent?
       private set

    /**
     * Relative Path to the resource
     */
    var relativePath: PathComponent?
        private set

    companion object {
        /**
         * Creates a new [Path] with the given [path] argument.
         * [path] is copied to both absolute and relative path components.
         */
        fun simplified(path: String): Path
    }
}
