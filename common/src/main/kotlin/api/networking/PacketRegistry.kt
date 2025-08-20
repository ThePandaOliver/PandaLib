/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("PacketRegistry")

package dev.pandasystems.pandalib.api.networking

import dev.pandasystems.pandalib.api.codec.StreamCodec
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.resources.ResourceLocation

@JvmField
internal val clientPacketHandlers = mutableMapOf<ResourceLocation, PacketHandler<CustomPacketPayload>>()

@JvmField
internal val serverPacketHandlers = mutableMapOf<ResourceLocation, PacketHandler<CustomPacketPayload>>()

@JvmField
internal val packetCodecs = mutableMapOf<ResourceLocation, StreamCodec<FriendlyByteBuf, CustomPacketPayload>>()

@JvmName("registerCodec")
fun <T : CustomPacketPayload> registerPacketCodec(id: ResourceLocation, codec: StreamCodec<FriendlyByteBuf, T>) {
	require(!packetCodecs.containsKey(id)) { "Packet type $id already has a codec" }
	@Suppress("UNCHECKED_CAST")
	packetCodecs[id] = codec as StreamCodec<FriendlyByteBuf, CustomPacketPayload>
}

@JvmName("registerHandler")
fun <T : CustomPacketPayload> registerPacketHandler(flow: PacketFlow, id: ResourceLocation, handler: PacketHandler<T>) {
	val handlers = when (flow) {
		PacketFlow.CLIENTBOUND -> clientPacketHandlers
		PacketFlow.SERVERBOUND -> serverPacketHandlers
	}
	require(!handlers.containsKey(id)) { "Packet type $id already has a handler" }
	@Suppress("UNCHECKED_CAST")
	handlers[id] = handler as PacketHandler<CustomPacketPayload>
}

@JvmName("registerHandler")
fun <T : CustomPacketPayload> registerPacketHandler(id: ResourceLocation, handler: PacketHandler<T>) {
	registerPacketHandler(PacketFlow.CLIENTBOUND, id, handler)
	registerPacketHandler(PacketFlow.SERVERBOUND, id, handler)
}