/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.packets

import dev.pandasystems.pandalib.networking.clientPacketHandlers
import dev.pandasystems.pandalib.networking.packetCodecs
import dev.pandasystems.pandalib.PandaLib
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.common.ClientCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

val clientboundPLPayloadPacketType = PacketType<ClientboundPLPayloadPacket>(PacketFlow.CLIENTBOUND, PandaLib.resourceLocation("pandalib_custom_payload"))

data class ClientboundPLPayloadPacket(val payload: CustomPacketPayload) : Packet<ClientCommonPacketListener> {
	override fun type(): PacketType<out Packet<ClientCommonPacketListener>> {
		return clientboundPLPayloadPacketType
	}

	override fun handle(handler: ClientCommonPacketListener) {
		clientPacketHandlers[payload.type()]!!.handle(payload)
	}
}

val clientboundPLPayloadCodec: StreamCodec<FriendlyByteBuf, ClientboundPLPayloadPacket> = StreamCodec.of(
	{ byteBuf, packet ->
//		byteBuf.writeUtf(packet.context.protocol.name, 16) // Writes the protocol name
//		byteBuf.writeBoolean(packet.context.flow == PacketFlow.CLIENTBOUND) // Writes a boolean to determine the flow
		byteBuf.writeResourceLocation(packet.payload.type().id) // Writes the payload type ID
		packetCodecs[packet.payload.type().id]!!.codec.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
//		val context = NetworkContext(
//			protocol = ConnectionProtocol.valueOf(byteBuf.readUtf(16)), // Reads the protocol name
//			flow = if (byteBuf.readBoolean()) PacketFlow.CLIENTBOUND else PacketFlow.SERVERBOUND // Reads a boolean to determine the flow
//		)
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = packetCodecs[payloadId]!!.codec.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ClientboundPLPayloadPacket(payload)
	}
)