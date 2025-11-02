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

import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacket
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ClientCommonPacketListener
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

object ServerConfigurationNetworking {
	internal val packetHandlers = mutableMapOf<ResourceLocation, ConfigurationPacketHandler<CustomPacketPayload>>()


	// Packet Registration

	fun <T : CustomPacketPayload> registerHandler(resourceLocation: ResourceLocation, handler: ConfigurationPacketHandler<T>) {
		require(!packetHandlers.containsKey(resourceLocation)) { "Packet type $resourceLocation already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[resourceLocation] = handler as ConfigurationPacketHandler<CustomPacketPayload>
	}


	// Packet Sending

	fun send(handler: ServerConfigurationPacketListenerImpl, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		send(handler, listOf(payload, *payloads))

	fun send(handler: ServerConfigurationPacketListenerImpl, payloads: Collection<CustomPacketPayload>) {
		payloads.forEach { handler.send(createPacket(it)) }
	}

	fun createPacket(payload: CustomPacketPayload): Packet<ClientCommonPacketListener> {
		return ClientboundPLPayloadPacket(payload)
	}

	fun interface ConfigurationPacketHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	interface Context {
		fun server(): MinecraftServer
		fun networkHandler(): ServerConfigurationPacketListenerImpl
		fun responseSender(): PacketSender
	}
}