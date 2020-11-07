package com.suparnatural.core.fs

import kotlin.test.Test
import kotlin.test.assertNull

actual var testDirectory: Path = "./build/tmp/jsTestContent".let {
    FileSystem.mkdir(it, true)
    Path.simplified(it)
}

class FileSystemJsTests {

    @Test
    fun testContentsDirectoryPaths() {
        assertNull(FileSystem.contentsDirectory.absolutePath!!.component)
        assertNull(FileSystem.contentsDirectory.relativePath!!.component)
    }

    @Test
    fun testCachesDirectoryPaths() {
        assertNull(FileSystem.cachesDirectory.absolutePath!!.component)
        assertNull(FileSystem.cachesDirectory.relativePath!!.component)
    }

    @Test
    fun testTemporaryDirectoryPaths() {
        assertNull(FileSystem.temporaryDirectory.absolutePath!!.component)
        assertNull(FileSystem.temporaryDirectory.relativePath!!.component)
    }

}

