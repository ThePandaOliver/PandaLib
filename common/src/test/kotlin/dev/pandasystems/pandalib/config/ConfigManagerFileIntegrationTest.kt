package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.store.FileConfigStore
import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
private data class IntegrationConfig(val greeting: String = "hello")

class ConfigManagerFileIntegrationTest {
    private lateinit var tempDir: Path

    @BeforeTest
    fun setUp() {
        tempDir = createTempDirectory("pandalib-config-integration")
    }

    @AfterTest
    fun tearDown() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `a value saved by one handle is visible to a handle loaded afterwards`() {
        val store = FileConfigStore(tempDir.resolve("config.json"))

        val first = ConfigManager.load(store, default = { IntegrationConfig() })
        first.update { it.copy(greeting = "goodbye") }
        first.save()

        val second = ConfigManager.load(store, default = { IntegrationConfig() })

        assertEquals("goodbye", second.value.greeting)
    }

    @Test
    fun `loading without a prior save writes the default value to disk`() {
        val store = FileConfigStore(tempDir.resolve("config.json"))

        val handle = ConfigManager.load(store, default = { IntegrationConfig() })

        assertEquals(IntegrationConfig(), handle.value)
        assertEquals(true, store.exists())
    }
}
