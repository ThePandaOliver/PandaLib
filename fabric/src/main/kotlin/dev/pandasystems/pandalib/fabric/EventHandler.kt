package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.handles.player.handle
import dev.pandasystems.pandalib.event.events.ServerPlayerBlockBreakEventContext
import dev.pandasystems.pandalib.event.events.ServerPlayerConnectionEventContext
import dev.pandasystems.pandalib.event.events.ServerPlayerRespawnEventContextFabric
import dev.pandasystems.pandalib.event.events.playerBlockBreakAfter
import dev.pandasystems.pandalib.event.events.playerBlockBreakBefore
import dev.pandasystems.pandalib.event.events.playerBlockBreakCanceled
import dev.pandasystems.pandalib.event.events.playerServerAfterRespawn
import dev.pandasystems.pandalib.event.events.playerServerJoin
import dev.pandasystems.pandalib.event.events.playerServerLeave
import dev.pandasystems.pandalib.event.events.server.ServerLevelEventContext
import dev.pandasystems.pandalib.event.events.server.ServerLifecycleEventContext
import dev.pandasystems.pandalib.event.events.server.ServerTickEventContext
import dev.pandasystems.pandalib.event.events.server.postServerTick
import dev.pandasystems.pandalib.event.events.server.preServerTick
import dev.pandasystems.pandalib.event.events.server.serverLevelLoad
import dev.pandasystems.pandalib.event.events.server.serverLevelUnLoad
import dev.pandasystems.pandalib.event.events.server.serverStarted
import dev.pandasystems.pandalib.event.events.server.serverStarting
import dev.pandasystems.pandalib.event.events.server.serverStopped
import dev.pandasystems.pandalib.event.events.server.serverStopping
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents

object EventHandler {
	fun init() {
		ServerLevelEvents.LOAD.register { _, level -> serverLevelLoad(ServerLevelEventContext(level)) }
		ServerLevelEvents.UNLOAD.register { _, level -> serverLevelUnLoad(ServerLevelEventContext(level)) }

		ServerLifecycleEvents.SERVER_STARTING.register { server -> serverStarting(ServerLifecycleEventContext(server)) }
		ServerLifecycleEvents.SERVER_STOPPING.register { server -> serverStopping(ServerLifecycleEventContext(server)) }
		ServerLifecycleEvents.SERVER_STARTED.register { server -> serverStarted(ServerLifecycleEventContext(server)) }
		ServerLifecycleEvents.SERVER_STOPPED.register { server -> serverStopped(ServerLifecycleEventContext(server)) }

		ServerTickEvents.START_SERVER_TICK.register { server -> preServerTick(ServerTickEventContext(server)) }
		ServerTickEvents.END_SERVER_TICK.register { server -> postServerTick(ServerTickEventContext(server)) }

		ServerPlayerEvents.JOIN.register { player -> playerServerJoin(ServerPlayerConnectionEventContext(player.handle())) }
		ServerPlayerEvents.LEAVE.register { player -> playerServerLeave(ServerPlayerConnectionEventContext(player.handle())) }
		ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive ->
			playerServerAfterRespawn(
				ServerPlayerRespawnEventContextFabric(
					oldPlayer.handle(),
					newPlayer.handle(),
					alive
				)
			)
		}

		PlayerBlockBreakEvents.BEFORE.register { level, player, blockPos, blockState, blockEntity ->
			playerBlockBreakBefore(
				ServerPlayerBlockBreakEventContext(
					level,
					player.handle(),
					blockPos,
					blockState,
					blockEntity
				)
			)
		}
		PlayerBlockBreakEvents.AFTER.register { level, player, pos, state, entity ->
			playerBlockBreakAfter(ServerPlayerBlockBreakEventContext(level, player.handle(), pos, state, entity))
		}
		PlayerBlockBreakEvents.CANCELED.register { level, player, pos, state, entity ->
			playerBlockBreakCanceled(ServerPlayerBlockBreakEventContext(level, player.handle(), pos, state, entity))
		}
	}
}