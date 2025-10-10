/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.interfaces.PacketSender
import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import org.jetbrains.annotations.ApiStatus

object ServerConfigurationNetworking {
	@JvmField
	internal val packetHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, ConfigurationPacketHandler<CustomPacketPayload>>()


	// Packet Registration

	@JvmStatic
	fun <T : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<T>, handler: ConfigurationPacketHandler<T>) {
		require(!packetHandlers.containsKey(type)) { "Packet type $type already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[type] = handler as ConfigurationPacketHandler<CustomPacketPayload>
	}


	// Packet Sending

	@JvmStatic
	fun send(handler: ServerConfigurationPacketListenerImpl, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		send(handler, listOf(payload, *payloads))

	@JvmStatic
	fun send(handler: ServerConfigurationPacketListenerImpl, payloads: Collection<CustomPacketPayload>) {
		handler.send(createPacket(*payloads.toTypedArray()))
	}

	@JvmStatic
	fun createPacket(vararg payloads: CustomPacketPayload): Packet<*> = ServerPlayNetworking.createPacket(*payloads)

	fun interface ConfigurationPacketHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	@ApiStatus.NonExtendable
	interface Context {
		fun server(): MinecraftServer
		fun networkHandler(): ServerConfigurationPacketListenerImpl
		fun responseSender(): PacketSender
	}
}