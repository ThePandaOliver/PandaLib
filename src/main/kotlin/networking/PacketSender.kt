/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
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