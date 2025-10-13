/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.platform.game
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus

object ClientConfigurationNetworking {
	internal lateinit var connection: Connection

	@JvmField
	internal val packetHandlers = mutableMapOf<ResourceLocation, ConfigurationPacketHandler<CustomPacketPayload>>()


	// Packet Registration

	@JvmStatic
	fun <T : CustomPacketPayload> registerHandler(resourceLocation: ResourceLocation, handler: ConfigurationPacketHandler<T>) {
		require(!packetHandlers.containsKey(resourceLocation)) { "Packet type $resourceLocation already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[resourceLocation] = handler as ConfigurationPacketHandler<CustomPacketPayload>
	}


	// Packet Sending

	@JvmStatic
	fun send(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(listOf(payload, *payloads))

	@JvmStatic
	fun send(payloads: Collection<CustomPacketPayload>) {
		require(game.isClient) { "Cannot send serverbound payloads from the server" }
		connection.send(createPacket(*payloads.toTypedArray()))
	}

	@JvmStatic
	fun createPacket(vararg payloads: CustomPacketPayload): Packet<*> = ClientPlayNetworking.createPacket(*payloads)

	fun interface ConfigurationPacketHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	@ApiStatus.NonExtendable
	interface Context {
		fun client(): Minecraft
		fun networkHandler(): ClientConfigurationPacketListenerImpl
		fun responseSender(): PacketSender
	}
}