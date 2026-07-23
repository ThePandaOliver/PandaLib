package dev.pandasystems.pandalib.config

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

interface ConfigStore {
    fun exists(): Boolean
    fun read(): ByteArray
    fun writeAtomically(content: ByteArray)
}

interface ConfigHandle<T> {
    val value: T

    fun reload(): T

    fun save()

    fun update(transform: (T) -> T): T
}