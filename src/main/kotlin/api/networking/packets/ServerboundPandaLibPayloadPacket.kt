/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.networking.packets

import dev.pandasystems.pandalib.api.networking.NetworkContext
import dev.pandasystems.pandalib.api.networking.PACKET_CODECS
import dev.pandasystems.pandalib.api.networking.SERVER_PACKET_HANDLERS
import dev.pandasystems.pandalib.core.PandaLib
import net.minecraft.network.ConnectionProtocol
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.common.ServerCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.common.custom.DiscardedPayload
import org.apache.logging.log4j.core.jmx.Server

data class ServerboundPandaLibPayloadPacket(val payload: CustomPacketPayload) : Packet<ServerCommonPacketListener> {
	override fun type(): PacketType<out Packet<ServerCommonPacketListener>> {
		return SERVERBOUND_PANDALIB_PAYLOAD_TYPE
	}

	override fun handle(handler: ServerCommonPacketListener) {
		SERVER_PACKET_HANDLERS[payload.type()]!!.handle(payload)
	}
}

@JvmField
val SERVERBOUND_PANDALIB_PAYLOAD_TYPE =
	PacketType<ServerboundPandaLibPayloadPacket>(PacketFlow.SERVERBOUND, PandaLib.resourceLocation("server_pandalib_custom_payload"))

@JvmField
val SERVERBOUND_PANDALIB_PAYLOAD_CODEC = StreamCodec.of<FriendlyByteBuf, ServerboundPandaLibPayloadPacket>(
	{ byteBuf, packet ->
//		byteBuf.writeUtf(packet.context.protocol.name, 16) // Writes the protocol name
//		byteBuf.writeBoolean(packet.context.flow == PacketFlow.CLIENTBOUND) // Writes a boolean to determine the flow
		byteBuf.writeResourceLocation(packet.payload.type().id) // Writes the payload type ID
		PACKET_CODECS[packet.payload.type().id]!!.codec.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
//		val context = NetworkContext(
//			protocol = ConnectionProtocol.valueOf(byteBuf.readUtf(16)), // Reads the protocol name
//			flow = if (byteBuf.readBoolean()) PacketFlow.CLIENTBOUND else PacketFlow.SERVERBOUND // Reads a boolean to determine the flow
//		)
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = PACKET_CODECS[payloadId]!!.codec.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ServerboundPandaLibPayloadPacket(payload)
	}
)