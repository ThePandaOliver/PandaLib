package dev.pandasystems.pandalib.networking

enum class PacketDirection {
    CLIENT_TO_SERVER,
    SERVER_TO_CLIENT,
}

class PacketType<T>(
    val id: PacketId,
    val direction: PacketDirection,
    val codec: PacketCodec<T>,
)