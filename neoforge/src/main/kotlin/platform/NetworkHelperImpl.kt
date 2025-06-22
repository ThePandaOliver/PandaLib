/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.impl.networking.NetworkContext
import dev.pandasystems.pandalib.impl.networking.PacketHandler
import dev.pandasystems.pandalib.api.platform.NetworkHelper
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.registration.PayloadRegistrar

class NetworkHelperImpl : PayloadRegistrar("1"), NetworkHelper {
	/* Register Packet handlers */

	override fun <T : CustomPacketPayload> registerClientPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: PacketHandler<T>
	) {
		playToClient<T>(type, codec) { arg: T, ctx: IPayloadContext -> receiver.handler(NetworkContext(ctx.player(), Env.CLIENT), arg) }
	}

	override fun <T : CustomPacketPayload> registerServerPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: PacketHandler<T>
	) {
		playToServer<T>(type, codec) { arg: T, ctx: IPayloadContext -> receiver.handler(NetworkContext(ctx.player(), Env.SERVER), arg) }
	}

	override fun <T : CustomPacketPayload> registerBiDirectionalPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: PacketHandler<T>,
		serverReceiver: PacketHandler<T>
	) {
		playBidirectional<T>(
			type, codec,
			DirectionalPayloadHandler<T>(
				{ arg: T, ctx: IPayloadContext -> clientReceiver.handler(NetworkContext(ctx.player(), Env.CLIENT), arg) },
				{ arg: T, ctx: IPayloadContext -> serverReceiver.handler(NetworkContext(ctx.player(), Env.SERVER), arg) }
			)
		)
	}
}
