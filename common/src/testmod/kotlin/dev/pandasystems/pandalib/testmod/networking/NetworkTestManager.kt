package dev.pandasystems.pandalib.testmod.networking

import dev.pandasystems.pandalib.event.events.server.ServerConfigurationEvents
import dev.pandasystems.pandalib.event.events.server.ServerPlayerEvents
import dev.pandasystems.pandalib.networking.ConfigurationNetworkManager
import dev.pandasystems.pandalib.networking.PlayPlayNetworkManager
import java.util.concurrent.atomic.AtomicBoolean

object NetworkTestManager {
    val configNetworkPassed = AtomicBoolean(false)
    val playNetworkPassed = AtomicBoolean(false)

    fun registerHandlers() {
        // --- 1. CONFIGURATION PHASE TEST ---
        // Register clientbound payload type on both sides
        ConfigurationNetworkManager.registerPayload(TestPackets.CONFIG_PING)

        // Server receives Pong -> validates and records success
        ConfigurationNetworkManager.register(TestPackets.CONFIG_PONG) { _, packet ->
            println("[TestMod] Server received config pong: ${packet.message}")
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
        // Register clientbound payload type on both sides
        PlayPlayNetworkManager.registerPayload(TestPackets.PLAY_PING)

        // Server receives Play Pong -> validates and records success
        PlayPlayNetworkManager.register(TestPackets.PLAY_PONG) { ctx, packet ->
            println("[TestMod] Server received play pong from player: ${ctx.player.scoreboardName}")
            if (packet.message == "play_pong_ack") {
                playNetworkPassed.set(true)
                println(">>> ALL NETWORK TESTS PASSED SUCCESSFULLY! <<<")
            }
        }

        // When a player finishes joining the world, initiate the Play Ping test
        ServerPlayerEvents.playerServerJoin.subscribe { ctx ->
            PlayPlayNetworkManager.sendToPlayer(ctx.player, TestPackets.PLAY_PING, PingPayload(42L, "hello_from_server"))
        }
    }
}
