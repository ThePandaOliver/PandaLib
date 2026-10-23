package dev.pandasystems.pandalib.testmod.networking

import dev.pandasystems.pandalib.client.networking.ClientConfigurationNetworkManager
import dev.pandasystems.pandalib.client.networking.ClientPlayNetworkManager

object ClientNetworkTestManager {
    fun registerClientHandlers() {
        // Client receives Configuration Ping -> replies with Configuration Pong
        ClientConfigurationNetworkManager.register(TestPackets.CONFIG_PING) { ctx, packet ->
            println("[TestMod] Client received config ping with nonce: ${packet.nonce}")
            ctx.reply(TestPackets.CONFIG_PONG, PongPayload(packet.nonce, "config_pong_ack"))
        }

        // Client receives Play Ping -> replies with Play Pong
        ClientPlayNetworkManager.register(TestPackets.PLAY_PING) { ctx, packet ->
            println("[TestMod] Client received play ping: ${packet.message}")
            ctx.reply(TestPackets.PLAY_PONG, PongPayload(packet.nonce, "play_pong_ack"))
        }
    }
}
