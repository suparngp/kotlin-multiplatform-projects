package com.suparnatural.core.fs

/**
 * Type of a file
 */
enum class FileType {
    /**
     * A regular file like text or pdf file.
     */
    Regular,

    /**
     * A directory.
     */
    Directory,

    /**
     * Platforms may support some special files. All those are marked as [Unknown].
     */
    Unknown
}

/**
 * Stats for a file at [path] with name as [name].
 */
data class StatResult(
        /**
         * The file's name including extension if any.
         */
        val name: String,

        /**
         * Absolute path to the file
         */

        val absolutePath: PathComponent,
        /**
         * Canonical path to the file. May differ from the input
         */
        val canonicalPath: PathComponent,

        /**
         * Date on which file was created.
         *
         * Note: On Android, this is always 0 as creation date is unavailable on all supported versions.
         */
        val createdAt: Double? = null,

        /**
         * Date when file was last modified.
         */
        val modifiedAt: Double? = null,

        /**
         * Size of the file in bytes
         */
        val size: Double? = null,

        /**
         * Type of the file.
         */
        val type: FileType
)
