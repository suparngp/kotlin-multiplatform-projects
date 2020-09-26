package com.suparnatural.core.fs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

actual var testDirectory: Path = FileSystem.contentsDirectory

class FileSystemAndroidTests {
    @Test
    fun testKnownPaths() {
        assertNotNull(FileSystem.contentsDirectory)
        val d = FileSystem.contentsDirectory
        val c = FileSystem.cachesDirectory
        val t = FileSystem.temporaryDirectory

        assertEquals(d.absolutePath!!.component, "/data/user/0/com.suparnatural.core.fs.test/files")
        assertEquals(c.absolutePath!!.component, "/data/user/0/com.suparnatural.core.fs.test/cache")
        assertEquals(t.absolutePath!!.component, "/data/user/0/com.suparnatural.core.fs.test/cache")

        assertEquals(d.relativePath!!.component, "/data/user/0/com.suparnatural.core.fs.test/files")
        assertEquals(c.relativePath!!.component, "/data/user/0/com.suparnatural.core.fs.test/cache")
        assertEquals(t.relativePath!!.component, "/data/user/0/com.suparnatural.core.fs.test/cache")
    }
}