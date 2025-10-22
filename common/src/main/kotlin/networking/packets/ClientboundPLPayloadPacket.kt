/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.networking.packets

import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.ClientPlayNetworking
import dev.pandasystems.pandalib.networking.PacketSender
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.packets.bundle.ServerboundPLBundlePacket
import dev.pandasystems.pandalib.utils.codec.StreamCodec
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketSendListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ClientCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class ClientboundPLPayloadPacket(val payload: CustomPacketPayload) : Packet<ClientCommonPacketListener> {
	override fun write(buffer: FriendlyByteBuf) = clientboundPLPayloadCodec.encode(buffer, this)

	override fun handle(handler: ClientCommonPacketListener) {
		class Sender(val connection: Connection): PacketSender {
			override fun createPacket(payloads: Collection<CustomPacketPayload>): Packet<*> {
				return if (payloads.size > 1) {
					ServerboundPLBundlePacket(payloads.map { ServerboundPLPayloadPacket(it) })
				} else {
					ServerboundPLPayloadPacket(payload)
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
			is ClientConfigurationPacketListenerImpl -> {
				val context = object : ClientConfigurationNetworking.Context {
					override fun client(): Minecraft = handler.minecraft
					override fun networkHandler(): ClientConfigurationPacketListenerImpl = handler
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ClientConfigurationNetworking.packetHandlers[payload.id()]?.receive(payload, context)
			}

			is ClientCommonPacketListenerImpl -> {
				val context = object : ClientPlayNetworking.Context {
					override fun client(): Minecraft = handler.minecraft
					override fun player(): LocalPlayer = requireNotNull(handler.minecraft.player) { "Player is null" }
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				ClientPlayNetworking.packetHandlers[payload.id()]?.receive(payload, context)
			}
		}
	}
}

val clientboundPLPayloadCodec: StreamCodec<FriendlyByteBuf, ClientboundPLPayloadPacket> = StreamCodec.of(
	{ byteBuf, packet ->
		byteBuf.writeResourceLocation(packet.payload.id()) // Writes the payload type ID
		PayloadCodecRegistry.packetCodecs[packet.payload.id()]!!.encode(byteBuf, packet.payload) // Encodes the payload using the registered codec
	},
	{ byteBuf ->
		val payloadId = byteBuf.readResourceLocation() // Reads the payload type ID
		val payload = PayloadCodecRegistry.packetCodecs[payloadId]!!.decode(byteBuf) // Decodes the payload using the registered codec
		return@of ClientboundPLPayloadPacket(payload)
	}
)