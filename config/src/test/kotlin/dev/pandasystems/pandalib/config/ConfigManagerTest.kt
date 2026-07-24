package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.codecs.JsonConfigCodec
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
private data class SampleConfig(val name: String = "default", val count: Int = 0)

class ConfigManagerTest {
    @Test
    fun `load creates and persists the default value when the store is empty`() {
        val store = InMemoryConfigStore()

        val handle = ConfigManager.load(store, default = { SampleConfig() })

        assertEquals(SampleConfig(), handle.value)
        assertTrue(store.exists())
    }

    @Test
    fun `load reads the existing value from the store without rewriting it`() {
        val store = InMemoryConfigStore()
        val codec = JsonConfigCodec()
        store.writeAtomically(codec.encode(SampleConfig.serializer(), SampleConfig(name = "existing", count = 5)))

        val handle = ConfigManager.load(store, default = { SampleConfig() }, codec = codec)

        assertEquals(SampleConfig(name = "existing", count = 5), handle.value)
        assertEquals(1, store.writeCount)
    }

    @Test
    fun `update mutates the in-memory value without writing to the store`() {
        val store = InMemoryConfigStore()
        val handle = ConfigManager.load(store, default = { SampleConfig() })
        val writesAfterLoad = store.writeCount

        val updated = handle.update { it.copy(count = it.count + 1) }

        assertEquals(1, updated.count)
        assertEquals(1, handle.value.count)
        assertEquals(writesAfterLoad, store.writeCount)
    }

    @Test
    fun `save persists the current value to the store`() {
        val store = InMemoryConfigStore()
        val codec = JsonConfigCodec()
        val handle = ConfigManager.load(store, default = { SampleConfig() }, codec = codec)
        handle.update { it.copy(name = "saved") }

        handle.save()

        val persisted = codec.decode(SampleConfig.serializer(), store.read())
        assertEquals("saved", persisted.name)
    }

    @Test
    fun `reload discards unsaved changes and re-reads from the store`() {
        val store = InMemoryConfigStore()
        val handle = ConfigManager.load(store, default = { SampleConfig() })
        handle.update { it.copy(name = "unsaved") }

        val reloaded = handle.reload()

        assertEquals(SampleConfig(), reloaded)
        assertEquals(SampleConfig(), handle.value)
    }
}
