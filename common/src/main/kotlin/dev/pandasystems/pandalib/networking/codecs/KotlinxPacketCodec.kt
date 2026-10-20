package dev.pandasystems.pandalib.networking.codecs

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.serializer

open class KotlinxPacketCodec<T>(
    private val serializer: KSerializer<T>,
    private val format: BinaryFormat,
) : PacketCodec<T> {
    override fun encode(value: T): ByteArray =
        format.encodeToByteArray(serializer, value)

    override fun decode(bytes: ByteArray): T =
        format.decodeFromByteArray(serializer, bytes)
}

inline fun <reified T> kotlinxPacketCodec(format: BinaryFormat): KotlinxPacketCodec<T> = KotlinxPacketCodec(serializer(), format)

@OptIn(ExperimentalSerializationApi::class)
class CborPacketCodec<T>(
    private val serializer: KSerializer<T>,
) : KotlinxPacketCodec<T>(serializer, Cbor)

inline fun <reified T> cborPacketCodec(): CborPacketCodec<T> = CborPacketCodec(serializer())
