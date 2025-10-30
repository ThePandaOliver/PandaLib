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

import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

object ClientConfigurationNetworking {
	internal lateinit var connection: Connection

	internal val packetHandlers = mutableMapOf<ResourceLocation, ConfigurationPacketHandler<CustomPacketPayload>>()


	// Packet Registration

	fun <T : CustomPacketPayload> registerHandler(resourceLocation: ResourceLocation, handler: ConfigurationPacketHandler<T>) {
		require(!packetHandlers.containsKey(resourceLocation)) { "Packet type $resourceLocation already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[resourceLocation] = handler as ConfigurationPacketHandler<CustomPacketPayload>
	}


	// Packet Sending

	fun send(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(listOf(payload, *payloads))

	fun send(payloads: Collection<CustomPacketPayload>) {
		require(gameEnvironment.isClient) { "Cannot send serverbound payloads from the server" }
		connection.send(createPacket(*payloads.toTypedArray()))
	}

	fun createPacket(vararg payloads: CustomPacketPayload): Packet<*> = ClientPlayNetworking.createPacket(*payloads)

	fun interface ConfigurationPacketHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	interface Context {
		fun client(): Minecraft
		fun networkHandler(): ClientConfigurationPacketListenerImpl
		fun responseSender(): PacketSender
	}
}