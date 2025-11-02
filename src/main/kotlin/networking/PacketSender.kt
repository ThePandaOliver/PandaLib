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
	fun sendPacket(packet: Packet<*>) {
		sendPacket(null, packet)
	}

	fun createPacket(payload: CustomPacketPayload): Packet<*>

	fun sendPacket(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
		sendPacket(listOf(payload, *payloads))
	}

	fun sendPacket(payloads: Collection<CustomPacketPayload>) {
		payloads.forEach { sendPacket(it) }
	}

	fun sendPacket(callback: ChannelFutureListener?, packet: Packet<*>)

	fun sendPacket(callback: ChannelFutureListener?, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
		sendPacket(callback, listOf(payload, *payloads))
	}

	fun sendPacket(callback: ChannelFutureListener?, payloads: Collection<CustomPacketPayload>) {
		payloads.forEach { sendPacket(callback, it) }
	}

	fun disconnect(disconnectReason: Component)
}