/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.utils.gameEnvironment
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketSendListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus

object ClientPlayNetworking {
	internal val packetHandlers = mutableMapOf<ResourceLocation, PlayPayloadHandler<CustomPacketPayload>>()


	// Packet Registration

	@JvmStatic
	fun <T : CustomPacketPayload> registerHandler(resourceLocation: ResourceLocation, handler: PlayPayloadHandler<T>) {
		require(!packetHandlers.containsKey(resourceLocation)) { "Packet type $resourceLocation already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[resourceLocation] = handler as PlayPayloadHandler<CustomPacketPayload>
	}


	// Packet Sending

	fun send(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(listOf(payload, *payloads))

	fun send(payloads: Collection<CustomPacketPayload>) {
		require(gameEnvironment.isClient) { "Cannot send serverbound payloads from the server" }
		val listener = requireNotNull(Minecraft.getInstance().player!!.connection)
		payloads.forEach { listener.send(createPacket(it)) }
	}

	fun createPacket(payload: CustomPacketPayload): Packet<*> {
		val friendlyByteBuf = FriendlyByteBuf(Unpooled.buffer())
		payload.write(friendlyByteBuf)
		return ServerboundCustomPayloadPacket(payload.id(), friendlyByteBuf)
	}


	// Packet Receiving

	fun handlePayload(handler: ClientPacketListener, payload: CustomPacketPayload) {
		class Sender(val connection: Connection): PacketSender {
			override fun createPacket(payload: CustomPacketPayload): Packet<*> {
				return ClientPlayNetworking.createPacket(payload)
			}

			override fun sendPacket(callback: PacketSendListener?, packet: Packet<*>) {
				connection.send(packet, callback)
			}

			override fun disconnect(disconnectReason: Component) {
				connection.disconnect(disconnectReason)
			}
		}

		val context = object : Context {
			override fun client(): Minecraft = handler.minecraft
			override fun player(): LocalPlayer = requireNotNull(handler.minecraft.player) { "Player is null" }
			override fun responseSender(): PacketSender = Sender(handler.connection)
		}
		packetHandlers[payload.id()]?.receive(payload, context)
	}

	fun interface PlayPayloadHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	@ApiStatus.NonExtendable
	interface Context {
		fun client(): Minecraft
		fun player(): LocalPlayer
		fun responseSender(): PacketSender
	}
}