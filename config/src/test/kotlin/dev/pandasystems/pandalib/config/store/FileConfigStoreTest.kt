package dev.pandasystems.pandalib.config.store

import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileConfigStoreTest {
    private lateinit var tempDir: Path

    @BeforeTest
    fun setUp() {
        tempDir = createTempDirectory("pandalib-config-test")
    }

    @AfterTest
    fun tearDown() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `exists is false before anything has been written`() {
        val store = FileConfigStore(tempDir.resolve("config.json"))

        assertFalse(store.exists())
    }

    @Test
    fun `writeAtomically creates missing parent directories`() {
        val path = tempDir.resolve("nested/deeper/config.json")
        val store = FileConfigStore(path)

        store.writeAtomically("hello".encodeToByteArray())

        assertTrue(path.exists())
        assertEquals("hello", path.readText())
    }

    @Test
    fun `writeAtomically overwrites existing content and leaves no temp files behind`() {
        val path = tempDir.resolve("config.json")
        val store = FileConfigStore(path)

        store.writeAtomically("first".encodeToByteArray())
        store.writeAtomically("second".encodeToByteArray())

        assertEquals("second", path.readText())
        val leftoverTempFiles = tempDir.listDirectoryEntries("config.json.*.tmp")
        assertTrue(leftoverTempFiles.isEmpty())
    }

    @Test
    fun `read returns exactly the bytes that were written`() {
        val path = tempDir.resolve("config.json")
        val store = FileConfigStore(path)
        store.writeAtomically(byteArrayOf(1, 2, 3))

        assertContentEquals(byteArrayOf(1, 2, 3), store.read())
    }

    @Test
    fun `exists is true once content has been written`() {
        val store = FileConfigStore(tempDir.resolve("config.json"))
        store.writeAtomically("data".encodeToByteArray())

        assertTrue(store.exists())
    }
}
