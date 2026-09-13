package dev.pandasystems.pandalib.neoforge.event.events.server

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.event.Event
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.events.server.ServerPlayerBlockBreakEventContext
import dev.pandasystems.pandalib.event.events.server.ServerPlayerConnectionEventContext
import dev.pandasystems.pandalib.event.events.server.ServerPlayerEvents
import dev.pandasystems.pandalib.event.events.server.ServerPlayerRespawnEventContext
import dev.pandasystems.pandalib.event.events.server.ServerPlayerRespawnEventContextForge
import dev.pandasystems.pandalib.neoforge.event.bindEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.player.PlayerEvent

@AutoService(ServerPlayerEvents::class)
class ServerPlayerEventsImpl : ServerPlayerEvents {
	override val playerServerJoin: Event<ServerPlayerConnectionEventContext> = NeoForge.EVENT_BUS.bindEvent(
		convertToCtx = { ServerPlayerConnectionEventContext(it.entity) },
		convertFromCtx = { PlayerEvent.PlayerLoggedInEvent(it.player) }
	)

	override val playerServerLeave: Event<ServerPlayerConnectionEventContext> = NeoForge.EVENT_BUS.bindEvent(
		convertToCtx = { ServerPlayerConnectionEventContext(it.entity) },
		convertFromCtx = { PlayerEvent.PlayerLoggedOutEvent(it.player) }
	)

	override val playerServerAfterRespawn: Event<ServerPlayerRespawnEventContext> = NeoForge.EVENT_BUS.bindEvent(
		convertToCtx = { ServerPlayerRespawnEventContextForge(it.entity) },
		convertFromCtx = { PlayerEvent.PlayerRespawnEvent(it.player, false) }
	)

	override val playerBlockBreakBefore: Event<ServerPlayerBlockBreakEventContext> = event()
	override val playerBlockBreakAfter: Event<ServerPlayerBlockBreakEventContext> = event()
	override val playerBlockBreakCanceled: Event<ServerPlayerBlockBreakEventContext> = event()
}
