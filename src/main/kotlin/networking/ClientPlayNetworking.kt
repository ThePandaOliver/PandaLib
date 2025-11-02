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

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacket
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.jetbrains.annotations.ApiStatus

object ClientPlayNetworking {
	internal val packetHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PlayPayloadHandler<CustomPacketPayload>>()


	// Packet Registration

	fun <T : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<T>, handler: PlayPayloadHandler<T>) {
		require(!packetHandlers.containsKey(type)) { "Packet type $type already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[type] = handler as PlayPayloadHandler<CustomPacketPayload>
	}


	// Packet Sending

	fun send(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(listOf(payload, *payloads))

	fun send(payloads: Collection<CustomPacketPayload>) {
		require(gameEnvironment.isClient) { "Cannot send serverbound payloads from the server" }
		val listener = requireNotNull(Minecraft.getInstance().player!!.connection)
		payloads.forEach { listener.send(createPacket(it)) }
	}

	fun createPacket(payload: CustomPacketPayload): Packet<*> {
		return ServerboundPLPayloadPacket(payload)
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