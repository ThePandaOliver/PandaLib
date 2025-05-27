
package dev.pandasystems.pandalib.core.event

import dev.architectury.event.events.common.PlayerEvent
import dev.pandasystems.pandalib.core.network.ConfigNetworking
import net.minecraft.server.level.ServerPlayer

object EventHandler {
	@JvmStatic
	fun init() {
		PlayerEvent.PLAYER_JOIN.register(::onServerPlayerJoin)
	}

	private fun onServerPlayerJoin(serverPlayer: ServerPlayer) {
		ConfigNetworking.syncCommonConfigs(serverPlayer)
	}
}
