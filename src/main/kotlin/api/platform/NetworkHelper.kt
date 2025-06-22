package dev.pandasystems.pandalib.api.platform

import dev.pandasystems.pandalib.impl.networking.NetworkReceiver
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

interface NetworkHelper {
	fun <T : CustomPacketPayload> sendToServer(payload: T)

	fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T)

	fun <T : CustomPacketPayload> sendToAllPlayers(payload: T)

	fun <T : CustomPacketPayload> registerClientReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	)

	fun <T : CustomPacketPayload> registerServerReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	)

	fun <T : CustomPacketPayload> registerBiDirectionalReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: NetworkReceiver<T>,
		serverReceiver: NetworkReceiver<T>
	)
}
