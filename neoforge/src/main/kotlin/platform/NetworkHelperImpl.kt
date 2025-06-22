package dev.pandasystems.pandalib.neoforge.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.impl.networking.NetworkContext
import dev.pandasystems.pandalib.impl.networking.NetworkReceiver
import dev.pandasystems.pandalib.api.platform.NetworkHelper
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.registration.PayloadRegistrar

class NetworkHelperImpl : PayloadRegistrar("1"), NetworkHelper {
	override fun <T : CustomPacketPayload> registerClientReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	) {
		playToClient<T>(type, codec) { arg: T, ctx: IPayloadContext -> receiver.receive(NetworkContext(ctx.player(), Env.CLIENT), arg) }
	}

	override fun <T : CustomPacketPayload> registerServerReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	) {
		playToServer<T>(type, codec) { arg: T, ctx: IPayloadContext -> receiver.receive(NetworkContext(ctx.player(), Env.SERVER), arg) }
	}

	override fun <T : CustomPacketPayload> registerBiDirectionalReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: NetworkReceiver<T>,
		serverReceiver: NetworkReceiver<T>
	) {
		playBidirectional<T>(
			type, codec,
			DirectionalPayloadHandler<T>(
				{ arg: T, ctx: IPayloadContext -> clientReceiver.receive(NetworkContext(ctx.player(), Env.CLIENT), arg) },
				{ arg: T, ctx: IPayloadContext -> serverReceiver.receive(NetworkContext(ctx.player(), Env.SERVER), arg) }
			)
		)
	}

	override fun <T : CustomPacketPayload> sendToServer(payload: T) {
		PacketDistributor.sendToServer(payload)
	}

	override fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T) {
		PacketDistributor.sendToPlayer(player, payload)
	}

	override fun <T : CustomPacketPayload> sendToAllPlayers(payload: T) {
		PacketDistributor.sendToAllPlayers(payload)
	}
}
