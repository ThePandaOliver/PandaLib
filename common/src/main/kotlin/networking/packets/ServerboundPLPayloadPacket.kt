/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.packets

import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerConfigurationNetworking
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import dev.pandasystems.pandalib.networking.PacketSender
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import io.netty.channel.ChannelFutureListener
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.common.ServerCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import net.minecraft.server.network.ServerGamePacketListenerImpl

val serverboundPLPayloadPacketType = PacketType<ServerboundPLPayloadPacket>(PacketFlow.SERVERBOUND, resourceLocation("pandalib_custom_payload"))

data class ServerboundPLPayloadPacket(val payload: CustomPacketPayload) : Packet<ServerCommonPacketListener> {
	override fun type(): PacketType<out Packet<ServerCommonPacketListener>> {
		return serverboundPLPayloadPacketType
	}

	override fun handle(handler: ServerCommonPacketListener) {
		class Sender(val connection: Connection): PacketSender {
			override fun createPacket(payloads: Collection<CustomPacketPayload>): Packet<*> {
				return if (payloads.size > 1) {
					ClientboundBundlePacket(payloads.map { ClientboundPLPayloadPacket(it) })
				} else {
					ClientboundPLPayloadPacket(payloads.first())
				}
			}

			override fun sendPacket(callback: ChannelFutureListener?, packet: Packet<*>) {
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
				ServerConfigurationNetworking.packetHandlers[payload.type()]?.receive(payload, context)
			}

			is ServerGamePacketListenerImpl -> {
				val context = object : ServerPlayNetworking.Context {
					override fun server(): MinecraftServer = handler.server
					override fun player(): ServerPlayer = handler.player
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ServerPlayNetworking.packetHandlers[payload.type()]?.receive(payload, context)
			}
		}
	}
}

val serverboundPLPayloadCodec: StreamCodec<FriendlyByteBuf, ServerboundPLPayloadPacket> = StreamCodec.of(
	{ byteBuf, packet ->
		byteBuf.writeResourceLocation(packet.payload.type().id) // Writes the payload type ID
		PayloadCodecRegistry.packetCodecs[packet.payload.type().id]!!.codec.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = PayloadCodecRegistry.packetCodecs[payloadId]!!.codec.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ServerboundPLPayloadPacket(payload)
	}
)