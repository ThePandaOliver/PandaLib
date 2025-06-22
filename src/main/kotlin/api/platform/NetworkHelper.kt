/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.platform

import dev.pandasystems.pandalib.impl.networking.PacketHandler
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

interface NetworkHelper {
	/* Register Packet handlers */

	fun <T : CustomPacketPayload> registerClientPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: PacketHandler<T>
	)

	fun <T : CustomPacketPayload> registerServerPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: PacketHandler<T>
	)

	fun <T : CustomPacketPayload> registerBiDirectionalPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: PacketHandler<T>,
		serverReceiver: PacketHandler<T>
	)
}
