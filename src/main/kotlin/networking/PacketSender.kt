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

import io.netty.channel.ChannelFutureListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface PacketSender {
	fun createPacket(payloads: Collection<CustomPacketPayload>): Packet<*>

	fun createPacket(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload): Packet<*> = createPacket(listOf(payload, *payloads))

	fun sendPacket(packet: Packet<*>) {
		sendPacket(null, packet)
	}

	fun sendPacket(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
		sendPacket(createPacket(payload, *payloads))
	}

	fun sendPacket(payloads: Collection<CustomPacketPayload>) {
		sendPacket(createPacket(payloads))
	}

	fun sendPacket(callback: ChannelFutureListener?, packet: Packet<*>)

	fun sendPacket(callback: ChannelFutureListener?, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
		sendPacket(callback, createPacket(payload, *payloads))
	}

	fun disconnect(disconnectReason: Component)
}