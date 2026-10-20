package dev.pandasystems.pandalib.testmod.networking

import dev.pandasystems.pandalib.event.events.server.ServerConfigurationEvents
import dev.pandasystems.pandalib.event.events.server.ServerPlayerEvents
import dev.pandasystems.pandalib.networking.ConfigurationNetworkManager
import dev.pandasystems.pandalib.networking.NetworkManager
import java.util.concurrent.atomic.AtomicBoolean

object NetworkTestManager {
    val configNetworkPassed = AtomicBoolean(false)
    val playNetworkPassed = AtomicBoolean(false)

    fun registerHandlers() {
        // --- 1. CONFIGURATION PHASE TEST ---
        // Server receives Ping -> replies with Pong
        ConfigurationNetworkManager.register(TestPackets.CONFIG_PING) { ctx, packet ->
            println("[TestMod] Server received config ping with nonce: ${packet.nonce}")
            ctx.reply(TestPackets.CONFIG_PONG, PongPayload(packet.nonce, "config_pong_ack"))
        }

        // Client receives Pong -> validates and records success
        ConfigurationNetworkManager.register(TestPackets.CONFIG_PONG) { ctx, packet ->
            println("[TestMod] Client received config pong: ${packet.message}")
            if (packet.message == "config_pong_ack") {
                configNetworkPassed.set(true)
            }
        }

        // Trigger: When configuration phase starts on the server, send the ping to client
        ServerConfigurationEvents.configurationStart.subscribe { ctx ->
            println("[TestMod] Starting configuration handshake for client...")
            ConfigurationNetworkManager.sendToClient(
                ctx.listener,
                TestPackets.CONFIG_PING,
                PingPayload(100L, "hello_config")
            )
        }

        // --- 2. PLAY PHASE TEST ---
        // Client receives Play Ping -> replies with Play Pong
        NetworkManager.register(TestPackets.PLAY_PING) { ctx, packet ->
            println("[TestMod] Client received play ping: ${packet.message}")
            ctx.reply(TestPackets.PLAY_PONG, PongPayload(packet.nonce, "play_pong_ack"))
        }

        // Server receives Play Pong -> validates and records success
        NetworkManager.register(TestPackets.PLAY_PONG) { ctx, packet ->
            println("[TestMod] Server received play pong from player: ${ctx.peer.scoreboardName}")
            if (packet.message == "play_pong_ack") {
                playNetworkPassed.set(true)
                println(">>> ALL NETWORK TESTS PASSED SUCCESSFULLY! <<<")
            }
        }

        // When a player finishes joining the world, initiate the Play Ping test
        ServerPlayerEvents.playerServerJoin.subscribe { ctx ->
            NetworkManager.sendToPlayer(ctx.player, TestPackets.PLAY_PING, PingPayload(42L, "hello_from_server"))
        }
    }
}