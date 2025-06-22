package dev.pandasystems.pandalib.impl.networking

import dev.pandasystems.pandalib.api.platform.networkHelper
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

fun <T : CustomPacketPayload> sendPayloadToServer(payload: T) {
	networkHelper.sendToServer(payload)
}

fun <T : CustomPacketPayload> sendPayloadToPlayer(player: ServerPlayer, payload: T) {
	networkHelper.sendToPlayer(player, payload)
}

fun <T : CustomPacketPayload> sendPayloadToAllPlayers(payload: T) {
	networkHelper.sendToAllPlayers(payload)
}
