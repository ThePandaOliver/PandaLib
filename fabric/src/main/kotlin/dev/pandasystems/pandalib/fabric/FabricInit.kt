package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.player.PlayerHandle
import dev.pandasystems.pandalib.core.player.ServerPlayerHandle
import dev.pandasystems.pandalib.event.events.serverStarted
import dev.pandasystems.pandalib.event.events.playerServerJoin
import dev.pandasystems.pandalib.event.events.playerServerLeave
import dev.pandasystems.pandalib.event.events.serverStarting
import dev.pandasystems.pandalib.event.events.serverStopping
import dev.pandasystems.pandalib.event.events.serverStopped
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

private class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandaLibMain(
            FabricNetworkManager()
        )

        setupEvents()
    }

    private fun setupEvents() {
        // Server events
        ServerLifecycleEvents.SERVER_STARTING.register { server -> serverStarting.invoke(server) }
        ServerLifecycleEvents.SERVER_STARTED.register { server -> serverStarted.invoke(server) }
        ServerLifecycleEvents.SERVER_STOPPING.register { server -> serverStopping.invoke(server) }
        ServerLifecycleEvents.SERVER_STOPPED.register { server -> serverStopped.invoke(server) }

        ServerPlayerEvents.JOIN.register { player -> playerServerJoin.invoke(ServerPlayerHandle(player.uuid)) }
        ServerPlayerEvents.LEAVE.register { player -> playerServerLeave.invoke(ServerPlayerHandle(player.uuid)) }
    }
}