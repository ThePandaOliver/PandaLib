/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.packets

import dev.pandasystems.pandalib.networking.PacketSender
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerConfigurationNetworking
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import dev.pandasystems.pandalib.utils.codec.StreamCodec
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketSendListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ServerCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import net.minecraft.server.network.ServerGamePacketListenerImpl

data class ServerboundPLPayloadPacket(val payload: CustomPacketPayload) : Packet<ServerGamePacketListener> {
	override fun write(buffer: FriendlyByteBuf) = serverboundPLPayloadCodec.encode(buffer, this)

	override fun handle(handler: ServerGamePacketListener) {
		class Sender(val connection: Connection): PacketSender {
			override fun createPacket(payloads: Collection<CustomPacketPayload>): Packet<*> {
				return if (payloads.size > 1) {
					ClientboundBundlePacket(payloads.map { ClientboundPLPayloadPacket(it) })
				} else {
					ClientboundPLPayloadPacket(payloads.first())
				}
			}

			override fun sendPacket(callback: PacketSendListener?, packet: Packet<*>) {
				connection.send(packet, callback)
			}

			override fun disconnect(disconnectReason: Component) {
				connection.disconnect(disconnectReason)
			}
		}

		when (handler) {
			is ServerConfigurationPacketListenerImpl -> {
				val context = object : ServerConfigurationNetworking.Context {
					override fun server(): MinecraftServer = handler.server
					override fun networkHandler(): ServerConfigurationPacketListenerImpl = handler
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ServerConfigurationNetworking.packetHandlers[payload.id()]?.receive(payload, context)
			}

			is ServerGamePacketListenerImpl -> {
				val context = object : ServerPlayNetworking.Context {
					override fun server(): MinecraftServer = handler.server
					override fun player(): ServerPlayer = handler.player
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ServerPlayNetworking.packetHandlers[payload.id()]?.receive(payload, context)
			}
		}
	}
}

val serverboundPLPayloadCodec: StreamCodec<FriendlyByteBuf, ServerboundPLPayloadPacket> = StreamCodec.of(
	{ byteBuf, packet ->
		byteBuf.writeResourceLocation(packet.payload.id()) // Writes the payload type ID
		PayloadCodecRegistry.packetCodecs[packet.payload.id()]!!.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = PayloadCodecRegistry.packetCodecs[payloadId]!!.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ServerboundPLPayloadPacket(payload)
	}
)