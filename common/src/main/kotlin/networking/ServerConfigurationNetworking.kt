/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacket
import dev.pandasystems.pandalib.networking.packets.bundle.ClientboundPLBundlePacket
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
		handler.send(createPacket(*payloads.toTypedArray()))
	}

	fun createPacket(vararg payloads: CustomPacketPayload): Packet<ClientCommonPacketListener> {
		require(payloads.isNotEmpty()) { "Requires at least one payload" }
		return if (payloads.size > 1) {
			ClientboundPLBundlePacket(payloads.map(::ClientboundPLPayloadPacket))
		} else {
			ClientboundPLPayloadPacket(payloads.first())
		}
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