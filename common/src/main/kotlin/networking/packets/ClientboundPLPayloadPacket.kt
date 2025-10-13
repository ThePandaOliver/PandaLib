/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.packets

import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.ClientPlayNetworking
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.PacketSender
import dev.pandasystems.pandalib.networking.packets.bundle.ServerboundPLBundlePacket
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import io.netty.channel.ChannelFutureListener
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.common.ClientCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

val clientboundPLPayloadPacketType = PacketType<ClientboundPLPayloadPacket>(PacketFlow.CLIENTBOUND, resourceLocation("pandalib_custom_payload"))

data class ClientboundPLPayloadPacket(val payload: CustomPacketPayload) : Packet<ClientCommonPacketListener> {
	override fun type(): PacketType<out Packet<ClientCommonPacketListener>> {
		return clientboundPLPayloadPacketType
	}

	override fun handle(handler: ClientCommonPacketListener) {
		class Sender(val connection: Connection): PacketSender {
			override fun createPacket(payloads: Collection<CustomPacketPayload>): Packet<*> {
				return if (payloads.size > 1) {
					ServerboundPLBundlePacket(payloads.map { ServerboundPLPayloadPacket(it) })
				} else {
					ServerboundPLPayloadPacket(payload)
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
			is ClientConfigurationPacketListenerImpl -> {
				val context = object : ClientConfigurationNetworking.Context {
					override fun client(): Minecraft = handler.minecraft
					override fun networkHandler(): ClientConfigurationPacketListenerImpl = handler
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ClientConfigurationNetworking.packetHandlers[payload.type()]?.receive(payload, context)
			}

			is ClientCommonPacketListenerImpl -> {
				val context = object : ClientPlayNetworking.Context {
					override fun client(): Minecraft = handler.minecraft
					override fun player(): LocalPlayer = requireNotNull(handler.minecraft.player) { "Player is null" }
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ClientPlayNetworking.packetHandlers[payload.type()]?.receive(payload, context)
			}
		}
	}
}

val clientboundPLPayloadCodec: StreamCodec<FriendlyByteBuf, ClientboundPLPayloadPacket> = StreamCodec.of(
	{ byteBuf, packet ->
		byteBuf.writeResourceLocation(packet.payload.type().id) // Writes the payload type ID
		PayloadCodecRegistry.packetCodecs[packet.payload.type().id]!!.codec.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = PayloadCodecRegistry.packetCodecs[payloadId]!!.codec.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ClientboundPLPayloadPacket(payload)
	}
)