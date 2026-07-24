package dev.pandasystems.pandalib.config.codecs

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@Serializable
private data class Nested(val flag: Boolean = false)

@Serializable
private data class CodecSample(
    val label: String = "",
    val values: List<Int> = emptyList(),
    val nested: Nested? = null,
)

class JsonConfigCodecTest {
    private val codec = JsonConfigCodec()

    @Test
    fun `formatName is json`() {
        assertEquals("json", codec.formatName)
    }

    @Test
    fun `encode then decode round trips the value`() {
        val value = CodecSample(label = "hi", values = listOf(1, 2, 3), nested = Nested(flag = true))

        val bytes = codec.encode(CodecSample.serializer(), value)
        val decoded = codec.decode(CodecSample.serializer(), bytes)

        assertEquals(value, decoded)
    }

    @Test
    fun `decode ignores unknown keys`() {
        val json = """{"label":"hi","values":[],"unknownField":"ignored"}"""

        val decoded = codec.decode(CodecSample.serializer(), json.encodeToByteArray())

        assertEquals("hi", decoded.label)
    }

    @Test
    fun `decode fills in missing fields with their defaults`() {
        val json = """{"label":"hi","values":[1]}"""

        val decoded = codec.decode(CodecSample.serializer(), json.encodeToByteArray())

        assertEquals(null, decoded.nested)
    }

    @Test
    fun `encode omits explicit nulls`() {
        val value = CodecSample(label = "hi", nested = null)

        val text = codec.encode(CodecSample.serializer(), value).decodeToString()

        assertFalse(text.contains("nested"))
    }

    @Test
    fun `encode includes values equal to their default`() {
        val value = CodecSample()

        val text = codec.encode(CodecSample.serializer(), value).decodeToString()

        assertEquals(true, text.contains("\"label\""))
    }
}
