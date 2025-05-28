package dev.pandasystems.pandalib.impl.networking

import dev.pandasystems.pandalib.impl.platform.Services
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

object PacketDistributor {
	@JvmStatic
	fun <T : CustomPacketPayload> sendToServer(payload: T) {
		Services.NETWORK.sendToServer(payload)
	}

	@JvmStatic
	fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T) {
		Services.NETWORK.sendToPlayer(player, payload)
	}

	@JvmStatic
	fun <T : CustomPacketPayload> sendToAllPlayers(payload: T) {
		Services.NETWORK.sendToAllPlayers(payload)
	}
}
