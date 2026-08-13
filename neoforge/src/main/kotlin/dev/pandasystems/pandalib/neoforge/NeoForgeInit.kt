package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.handles.player.handle
import dev.pandasystems.pandalib.core.modId
import dev.pandasystems.pandalib.event.events.*
import dev.pandasystems.pandalib.event.events.server.serverStarted
import dev.pandasystems.pandalib.event.events.server.serverStarting
import dev.pandasystems.pandalib.event.events.server.serverStopped
import dev.pandasystems.pandalib.event.events.server.serverStopping
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeNetworkManager
import net.neoforged.bus.EventBus
import net.neoforged.fml.common.Mod

@Mod(modId)
private class NeoForgeInit(
    eventBus: EventBus
) {
    init {
        PandaLibMain(
            NeoForgeNetworkManager(),
            NeoForgeRuntime()
        )

        setupEvents(eventBus)
    }

    private fun setupEvents(eventBus: EventBus) {
        // Server events
        ServerLifecycleEvents.SERVER_STARTING.register { server -> serverStarting.invoke(server) }
        ServerLifecycleEvents.SERVER_STARTED.register { server -> serverStarted.invoke(server) }
        ServerLifecycleEvents.SERVER_STOPPING.register { server -> serverStopping.invoke(server) }
        ServerLifecycleEvents.SERVER_STOPPED.register { server -> serverStopped.invoke(server) }

        ServerPlayerEvents.JOIN.register { player -> playerServerJoin.invoke(player.handle()) }
        ServerPlayerEvents.LEAVE.register { player -> playerServerLeave.invoke(player.handle()) }
        ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive -> playerServerAfterRespawn.invoke(oldPlayer.handle(), newPlayer.handle(), alive) }
        ServerPlayerEvents.COPY_FROM.register { oldPlayer, newPlayer, alive -> playerServerCopyFrom.invoke(oldPlayer.handle(), newPlayer.handle(), alive) }

        PlayerBlockBreakEvents.BEFORE.register { level, player, pos, state, _ -> playerBlockBreakBefore.invoke(level, player.handle(), pos, state) }
        PlayerBlockBreakEvents.AFTER.register { level, player, pos, state, _ -> playerBlockBreakAfter.invoke(level, player.handle(), pos, state) }
        PlayerBlockBreakEvents.CANCELED.register { level, player, pos, state, _ -> playerBlockBreakCanceled.invoke(level, player.handle(), pos, state) }
    }
}