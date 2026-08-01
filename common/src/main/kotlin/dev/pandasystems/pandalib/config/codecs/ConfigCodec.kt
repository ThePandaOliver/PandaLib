package dev.pandasystems.pandalib.config.codecs

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

interface ConfigCodec {
    val formatName: String

    fun <T> encode(serializer: SerializationStrategy<T>, value: T): ByteArray

    fun <T> decode(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray,
    ): T
}