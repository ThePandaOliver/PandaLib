package me.pandamods.pandalib.core.client.event

import dev.architectury.event.events.client.ClientPlayerEvent
import me.pandamods.pandalib.core.network.ConfigNetworking
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.player.LocalPlayer

@Environment(EnvType.CLIENT)
object EventHandlerClient {
	fun init() {
		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(ClientPlayerEvent.ClientPlayerJoin { obj: LocalPlayer -> onClientPlayerJoin(obj) })
	}

	private fun onClientPlayerJoin(localPlayer: LocalPlayer) {
		ConfigNetworking.SyncClientConfigs()
	}
}
