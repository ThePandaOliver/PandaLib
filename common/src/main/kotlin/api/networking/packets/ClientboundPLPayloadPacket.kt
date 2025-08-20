/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.networking.packets

import dev.pandasystems.pandalib.api.codec.StreamCodec
import dev.pandasystems.pandalib.api.networking.CustomPacketPayload
import dev.pandasystems.pandalib.api.networking.clientPacketHandlers
import dev.pandasystems.pandalib.api.networking.packetCodecs
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener

data class ClientboundPLPayloadPacket(val payload: CustomPacketPayload) : Packet<ClientGamePacketListener> {
	override fun write(buffer: FriendlyByteBuf) {
		buffer.writeResourceLocation(payload.id())
		payload.write(buffer)
	}

	override fun handle(handler: ClientGamePacketListener) {
		clientPacketHandlers[payload.id()]!!.handle(payload)
	}
}

val clientboundPLPayloadCodec: StreamCodec<FriendlyByteBuf, ClientboundPLPayloadPacket> = StreamCodec.of(
	{ byteBuf, packet ->
//		byteBuf.writeUtf(packet.context.protocol.name, 16) // Writes the protocol name
//		byteBuf.writeBoolean(packet.context.flow == PacketFlow.CLIENTBOUND) // Writes a boolean to determine the flow
		byteBuf.writeResourceLocation(packet.payload.id()) // Writes the payload type ID
		packetCodecs[packet.payload.id()]!!.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
//		val context = NetworkContext(
//			protocol = ConnectionProtocol.valueOf(byteBuf.readUtf(16)), // Reads the protocol name
//			flow = if (byteBuf.readBoolean()) PacketFlow.CLIENTBOUND else PacketFlow.SERVERBOUND // Reads a boolean to determine the flow
//		)
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = packetCodecs[payloadId]!!.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ClientboundPLPayloadPacket(payload)
	}
)