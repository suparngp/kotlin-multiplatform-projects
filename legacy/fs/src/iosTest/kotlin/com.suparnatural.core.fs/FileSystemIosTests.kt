package com.suparnatural.core.fs

import kotlin.test.Test
import kotlin.test.assertEquals
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

    @Test
    fun testSimplifiedPath() {
        val paths = mapOf("/" to "/", "/var" to "/var/", "/var/file.txt" to "/var/file.txt")
        paths.keys.forEach {
            val path = Path.simplified(it)
            assertEquals(it, path.absolutePath!!.component!!)
            assertEquals(it, path.relativePath!!.component!!)
            assertEquals("file://${paths[it]}", path.absolutePath!!.url!!.absoluteString)
            assertEquals("file://${paths[it]}", path.relativePath!!.url!!.absoluteString)
        }
    }

    @Test
    fun testUrlFromString() {
        val paths = mapOf("/" to "/", "/var" to "/var/", "/.var" to "/.var", "/var/file.txt" to "/var/file.txt")
        paths.keys.forEach {
            val url = Path.urlFromString(it)!!
            assertEquals("file://${paths[it]}", url.absoluteString)
        }
    }

}