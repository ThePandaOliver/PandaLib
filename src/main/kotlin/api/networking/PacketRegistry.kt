/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("PacketRegistry")

package dev.pandasystems.pandalib.api.networking

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

@JvmField
internal val clientPacketHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PacketHandler<CustomPacketPayload>>()

@JvmField
internal val serverPacketHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PacketHandler<CustomPacketPayload>>()

@JvmField
internal val packetCodecs = mutableMapOf<ResourceLocation, CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>>()

@JvmName("registerCodec")
fun <T : CustomPacketPayload> registerPacketCodec(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>) {
	require(!packetCodecs.containsKey(type.id)) { "Packet type $type already has a codec" }
	@Suppress("UNCHECKED_CAST")
	packetCodecs[type.id] = CustomPacketPayload.TypeAndCodec(type, codec) as CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>
}

@JvmName("registerHandler")
fun <T : CustomPacketPayload> registerPacketHandler(flow: PacketFlow, type: CustomPacketPayload.Type<T>, handler: PacketHandler<T>) {
	val handlers = when (flow) {
		PacketFlow.CLIENTBOUND -> clientPacketHandlers
		PacketFlow.SERVERBOUND -> serverPacketHandlers
	}
	require(!handlers.containsKey(type)) { "Packet type $type already has a handler" }
	@Suppress("UNCHECKED_CAST")
	handlers[type] = handler as PacketHandler<CustomPacketPayload>
}

@JvmName("registerHandler")
fun <T : CustomPacketPayload> registerPacketHandler(type: CustomPacketPayload.Type<T>, handler: PacketHandler<T>) {
	registerPacketHandler(PacketFlow.CLIENTBOUND, type, handler)
	registerPacketHandler(PacketFlow.SERVERBOUND, type, handler)
}