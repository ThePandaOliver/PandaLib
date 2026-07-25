package dev.pandasystems.pandalib.networking.codecs

interface PacketCodec<T> {
    fun encode(value: T): ByteArray
    fun decode(bytes: ByteArray): T
}