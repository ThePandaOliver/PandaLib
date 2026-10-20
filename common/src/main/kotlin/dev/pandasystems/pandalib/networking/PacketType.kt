package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.networking.codecs.PacketCodec

class PacketType<T>(
    val id: Identifier,
    val direction: PacketDirection,
    val codec: PacketCodec<T>,
    val phase: NetworkPhase = NetworkPhase.PLAY,
)

enum class PacketDirection {
    CLIENT_TO_SERVER,
    SERVER_TO_CLIENT
}

enum class NetworkPhase {
    PLAY,
    CONFIGURATION
}
