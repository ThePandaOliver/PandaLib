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
) {
    override fun toString(): String = "PacketType(id=$id, direction=$direction)"
}

/** Creates a packet sent from the client to the server. */
fun <T> serverboundPacket(id: PacketId, codec: PacketCodec<T>): PacketType<T> =
    PacketType(id, PacketDirection.CLIENT_TO_SERVER, codec)

/** Creates a packet sent from the server to a client. */
fun <T> clientboundPacket(id: PacketId, codec: PacketCodec<T>): PacketType<T> =
    PacketType(id, PacketDirection.SERVER_TO_CLIENT, codec)
