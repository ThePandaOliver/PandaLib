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
internal val CLIENT_PACKET_HANDLERS = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PacketHandler<*>>()

@JvmField
internal val CLIENT_PACKET_CODEC = mutableMapOf<ResourceLocation, CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>>()

@JvmField
internal val SERVER_PACKET_HANDLERS = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PacketHandler<*>>()

@JvmField
internal val SERVER_PACKET_CODEC = mutableMapOf<ResourceLocation, CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>>()

@JvmName("registerPlayHandler")
fun <T : CustomPacketPayload> registerPlayPacketHandler(flow: PacketFlow, type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>, handler: PacketHandler<T>) {
	val handlers = when (flow) {
		PacketFlow.CLIENTBOUND -> CLIENT_PACKET_HANDLERS
		PacketFlow.SERVERBOUND -> SERVER_PACKET_HANDLERS
	}
	val codecs = when (flow) {
		PacketFlow.CLIENTBOUND -> CLIENT_PACKET_CODEC
		PacketFlow.SERVERBOUND -> SERVER_PACKET_CODEC
	}
	require(!handlers.containsKey(type)) { "Packet type $type already has a handler" }
	require(!codecs.containsKey(type.id)) { "Packet type $type already has a codec" }
	handlers[type] = handler
	@Suppress("UNCHECKED_CAST")
	codecs[type.id] = CustomPacketPayload.TypeAndCodec(type, codec) as CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>
}

@JvmName("registerPlayHandler")
fun <T : CustomPacketPayload> registerPlayPacketHandler(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>, handler: PacketHandler<T>) {
	registerPlayPacketHandler(PacketFlow.CLIENTBOUND, type, codec, handler)
	registerPlayPacketHandler(PacketFlow.SERVERBOUND, type, codec, handler)
}