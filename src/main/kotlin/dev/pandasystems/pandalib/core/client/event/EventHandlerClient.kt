package dev.pandasystems.pandalib.core.client.event

import dev.architectury.event.events.client.ClientPlayerEvent
import dev.pandasystems.pandalib.core.network.ConfigNetworking
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.player.LocalPlayer

@Environment(EnvType.CLIENT)
object EventHandlerClient {
	@JvmStatic
	fun init() {
		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(::onClientPlayerJoin)
	}

	private fun onClientPlayerJoin(localPlayer: LocalPlayer) {
		ConfigNetworking.syncClientConfigs()
	}
}
