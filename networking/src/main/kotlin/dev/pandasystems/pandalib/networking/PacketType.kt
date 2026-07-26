package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.codecs.PacketCodec

enum class PacketDirection {
    CLIENT_TO_SERVER,
    SERVER_TO_CLIENT,
}

class PacketType<T>(
    val id: PacketId,
    val direction: PacketDirection,
    val codec: PacketCodec<T>,
)