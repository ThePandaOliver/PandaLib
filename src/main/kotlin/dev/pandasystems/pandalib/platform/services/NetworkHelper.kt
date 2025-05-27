
package dev.pandasystems.pandalib.platform.services

import dev.pandasystems.pandalib.networking.NetworkRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

interface NetworkHelper : NetworkRegistry {
	fun <T : CustomPacketPayload> sendToServer(payload: T)

	fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T)

	fun <T : CustomPacketPayload> sendToAllPlayers(payload: T)
}
