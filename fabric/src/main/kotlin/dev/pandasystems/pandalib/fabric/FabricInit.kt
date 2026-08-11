package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.handles.player.handle
import dev.pandasystems.pandalib.event.events.*
import dev.pandasystems.pandalib.fabric.networking.FabricNetworkManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents

private class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandaLibMain(
	        FabricNetworkManager(),
            FabricRuntime()
        )

        setupEvents()
    }

    private fun setupEvents() {
        // Server events
        ServerLifecycleEvents.SERVER_STARTING.register { server -> serverStarting.invoke(server) }
        ServerLifecycleEvents.SERVER_STARTED.register { server -> serverStarted.invoke(server) }
        ServerLifecycleEvents.SERVER_STOPPING.register { server -> serverStopping.invoke(server) }
        ServerLifecycleEvents.SERVER_STOPPED.register { server -> serverStopped.invoke(server) }
        ServerLifecycleEvents.BEFORE_SAVE.register { server, flush, force -> serverBeforeSave.invoke(server, flush, force) }
        ServerLifecycleEvents.AFTER_SAVE.register { server, flush, force -> serverAfterSave.invoke(server, flush, force) }

        ServerPlayerEvents.JOIN.register { player -> playerServerJoin.invoke(player.handle()) }
        ServerPlayerEvents.LEAVE.register { player -> playerServerLeave.invoke(player.handle()) }
        ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive -> playerServerAfterRespawn.invoke(oldPlayer.handle(), newPlayer.handle(), alive) }
        ServerPlayerEvents.COPY_FROM.register { oldPlayer, newPlayer, alive -> playerServerCopyFrom.invoke(oldPlayer.handle(), newPlayer.handle(), alive) }

        PlayerBlockBreakEvents.BEFORE.register { level, player, pos, state, _ -> playerBlockBreakBefore.invoke(level, player.handle(), pos, state) }
        PlayerBlockBreakEvents.AFTER.register { level, player, pos, state, _ -> playerBlockBreakAfter.invoke(level, player.handle(), pos, state) }
        PlayerBlockBreakEvents.CANCELED.register { level, player, pos, state, _ -> playerBlockBreakCanceled.invoke(level, player.handle(), pos, state) }
    }
}