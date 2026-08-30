/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.mixin.network

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.networking.ClientPlayNetworking
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket
import net.minecraft.server.network.ServerGamePacketListenerImpl

object CustomPayloadKtImpl {
	@JvmStatic
	fun handleClient(listener: ClientPacketListener, packet: ClientboundCustomPayloadPacket): Boolean {
		if (packet.identifier !in PayloadCodecRegistry.packetCodecs) return false
		val data = packet.data
		data.markReaderIndex()
		return try {
			val payload = PayloadCodecRegistry.decode(packet.identifier, data) ?: return true
			ClientPlayNetworking.handlePayload(listener, payload)
			true
		} catch (e: Exception) {
			PandaLib.logger.error("Failed to handle clientbound PandaLib payload {}", packet.identifier, e)
			true
		} finally {
			data.resetReaderIndex()
		}
	}

	@JvmStatic
	fun handleServer(listener: ServerGamePacketListenerImpl, packet: ServerboundCustomPayloadPacket): Boolean {
		if (packet.identifier !in PayloadCodecRegistry.packetCodecs) return false
		val data = packet.data
		data.markReaderIndex()
		return try {
			val payload = PayloadCodecRegistry.decode(packet.identifier, data) ?: return true
			ServerPlayNetworking.handlePayload(listener, payload)
			true
		} catch (e: Exception) {
			PandaLib.logger.error("Failed to handle serverbound PandaLib payload {}", packet.identifier, e)
			true
		} finally {
			data.resetReaderIndex()
		}
	}
}
