package com.suparnatural.core.fs

import org.junit.Test
import java.nio.file.Paths
import kotlin.test.assertNull

actual var testDirectory: Path = Path.simplified(Paths.get("./build/tmp/jvmTestContent").toAbsolutePath().toString())

class FileSystemJvmTests {

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

