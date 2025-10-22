/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
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