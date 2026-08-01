package dev.pandasystems.pandalib.config.codecs

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

class JsonConfigCodec(
    private val json: Json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
        explicitNulls = false
    },
) : ConfigCodec {
    override val formatName: String = "json"

    override fun <T> encode(
        serializer: SerializationStrategy<T>,
        value: T,
    ): ByteArray =
        json.encodeToString(serializer, value).encodeToByteArray()

    override fun <T> decode(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray,
    ): T =
        json.decodeFromString(deserializer, bytes.decodeToString())
}