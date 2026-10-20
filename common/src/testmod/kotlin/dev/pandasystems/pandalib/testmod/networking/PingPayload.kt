package dev.pandasystems.pandalib.testmod.networking

import dev.pandasystems.pandalib.core.handles.identifier
import dev.pandasystems.pandalib.networking.*
import dev.pandasystems.pandalib.networking.codecs.CborPacketCodec
import dev.pandasystems.pandalib.networking.codecs.KotlinxPacketCodec
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json

@Serializable
data class PingPayload(val nonce: Long, val message: String)

@Serializable
data class PongPayload(val nonce: Long, val message: String)

object TestPackets {
    // 1. Configuration Packets (Pre-game handshake)
    val CONFIG_PING = PacketType(
        id = identifier("pandalib_test", "config_ping"),
        direction = PacketDirection.SERVER_TO_CLIENT,
        codec = CborPacketCodec(PingPayload.serializer()),
        phase = NetworkPhase.CONFIGURATION
    )

    val CONFIG_PONG = PacketType(
        id = identifier("pandalib_test", "config_pong"),
        direction = PacketDirection.CLIENT_TO_SERVER,
        codec = CborPacketCodec(PongPayload.serializer()),
        phase = NetworkPhase.CONFIGURATION
    )

    // 2. Play Packets (In-world)
    val PLAY_PING = PacketType(
        id = identifier("pandalib_test", "play_ping"),
        direction = PacketDirection.SERVER_TO_CLIENT,
        codec = CborPacketCodec(PingPayload.serializer()),
        phase = NetworkPhase.PLAY
    )

    val PLAY_PONG = PacketType(
        id = identifier("pandalib_test", "play_pong"),
        direction = PacketDirection.CLIENT_TO_SERVER,
        codec = CborPacketCodec(PongPayload.serializer()),
        phase = NetworkPhase.PLAY
    )
}