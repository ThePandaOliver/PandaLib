/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.neoforge.platform

import dev.architectury.utils.Env
import me.pandamods.pandalib.event.events.NetworkingEvents
import me.pandamods.pandalib.networking.NetworkContext
import me.pandamods.pandalib.networking.NetworkReceiver
import me.pandamods.pandalib.platform.Services
import me.pandamods.pandalib.platform.services.NetworkHelper
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

	companion object {
		fun registerPackets(event: RegisterPayloadHandlersEvent) {
			NetworkingEvents.PACKET_PAYLOAD_REGISTRY.invoker().register(Services.NETWORK)
		}
	}
}
