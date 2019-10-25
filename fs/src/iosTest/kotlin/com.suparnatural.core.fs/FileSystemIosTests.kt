package com.suparnatural.core.fs

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

actual var testDirectory: Path = FileSystem.contentsDirectory

class FileSystemIosTests {

    @Test
    fun testKnownPaths() {
        assertNotNull(FileSystem.contentsDirectory)
        val d = FileSystem.contentsDirectory
        val c = FileSystem.cachesDirectory
        val t = FileSystem.temporaryDirectory

        val paths = listOf(d, c, t)
        paths.forEach {
            assertNotNull(it.absolutePath)
            assertNotNull(it.relativePath)
            assertTrue(it.absolutePath!!.component!!.isNotEmpty())
            assertTrue(it.relativePath!!.component!!.isNotEmpty())
        }
    }

}