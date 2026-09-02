package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.handles.player.handle
import dev.pandasystems.pandalib.event.events.ServerPlayerBlockBreakEventContext
import dev.pandasystems.pandalib.event.events.ServerPlayerConnectionEventContext
import dev.pandasystems.pandalib.event.events.ServerPlayerRespawnEventContext
import dev.pandasystems.pandalib.event.events.ServerPlayerRespawnEventContextForge
import dev.pandasystems.pandalib.event.events.playerBlockBreakBefore
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
import net.minecraft.world.entity.EntityEvent
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.event.level.block.BreakBlockEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent
import net.neoforged.neoforge.event.server.ServerStoppingEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent

object EventHandler {
	fun init(eventBus: IEventBus) {
		eventBus.register(this)
	}

	@SubscribeEvent
	fun onLevelLoad(event: LevelEvent.Load) {
		serverLevelLoad(ServerLevelEventContext(event.level))
	}

	@SubscribeEvent
	fun onLevelUnload(event: LevelEvent.Unload) {
		serverLevelUnLoad(ServerLevelEventContext(event.level))
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent) {
		serverStarting(ServerLifecycleEventContext(event.server))
	}

	@SubscribeEvent
	fun onServerStarted(event: ServerStartedEvent) {
		serverStarted(ServerLifecycleEventContext(event.server))
	}

	@SubscribeEvent
	fun onServerStopping(event: ServerStoppingEvent) {
		serverStopping(ServerLifecycleEventContext(event.server))
	}

	@SubscribeEvent
	fun onServerStopped(event: ServerStoppedEvent) {
		serverStopped(ServerLifecycleEventContext(event.server))
	}

	@SubscribeEvent
	fun onPreServerTick(event: ServerTickEvent.Pre) {
		preServerTick(ServerTickEventContext(event.server))
	}

	@SubscribeEvent
	fun onPostServerTick(event: ServerTickEvent.Post) {
		postServerTick(ServerTickEventContext(event.server))
	}

	@SubscribeEvent
	fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
		playerServerJoin(ServerPlayerConnectionEventContext(event.entity.handle()))
	}

	@SubscribeEvent
	fun onPlayerLeave(event: PlayerEvent.PlayerLoggedOutEvent) {
		playerServerLeave(ServerPlayerConnectionEventContext(event.entity.handle()))
	}

	@SubscribeEvent
	fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
		playerServerAfterRespawn(ServerPlayerRespawnEventContextForge(event.entity.handle()))
	}
}