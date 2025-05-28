package dev.pandasystems.pandalib.impl.networking

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@Suppress("unused")
interface NetworkRegistry {
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
