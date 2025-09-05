/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import net.minecraft.network.ConnectionProtocol
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

object PacketRegistry {
	@JvmField
	internal val clientPacketHandlers =
		mutableMapOf<ConnectionProtocol, MutableMap<CustomPacketPayload.Type<out CustomPacketPayload>, PacketHandler<CustomPacketPayload>>>()

	@JvmField
	internal val serverPacketHandlers =
		mutableMapOf<ConnectionProtocol, MutableMap<CustomPacketPayload.Type<out CustomPacketPayload>, PacketHandler<CustomPacketPayload>>>()

	@JvmField
	internal val packetCodecs = mutableMapOf<ResourceLocation, CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>>()

	@JvmStatic
	fun <T : CustomPacketPayload> registerCodec(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>) {
		require(!packetCodecs.containsKey(type.id)) { "Packet type $type already has a codec" }
		@Suppress("UNCHECKED_CAST")
		packetCodecs[type.id] = CustomPacketPayload.TypeAndCodec(type, codec) as CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>
	}

	@JvmStatic
	@JvmOverloads
	fun <T : CustomPacketPayload> registerHandler(
		flow: PacketFlow, protocol: ConnectionProtocol = ConnectionProtocol.PLAY,
		type: CustomPacketPayload.Type<T>, handler: PacketHandler<T>
	) {
		val handlersMap = when (flow) {
			PacketFlow.CLIENTBOUND -> clientPacketHandlers
			PacketFlow.SERVERBOUND -> serverPacketHandlers
		}
		val handlers = handlersMap.computeIfAbsent(protocol) { mutableMapOf() }
		require(!handlers.containsKey(type)) { "Packet type $type already has a handler" }
		@Suppress("UNCHECKED_CAST")
		handlers[type] = handler as PacketHandler<CustomPacketPayload>
	}

	@JvmStatic
	fun <T : CustomPacketPayload> registerHandler(protocol: ConnectionProtocol = ConnectionProtocol.PLAY, type: CustomPacketPayload.Type<T>, handler: PacketHandler<T>) {
		registerHandler(PacketFlow.CLIENTBOUND, protocol, type, handler)
		registerHandler(PacketFlow.SERVERBOUND, protocol, type, handler)
	}
}