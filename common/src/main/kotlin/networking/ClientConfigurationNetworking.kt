/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import com.mojang.authlib.minecraft.client.MinecraftClient
import dev.pandasystems.pandalib.networking.interfaces.PacketSender
import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacket
import dev.pandasystems.pandalib.networking.packets.bundle.ServerboundPLBundlePacket
import dev.pandasystems.pandalib.platform.game
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ServerGamePacketListener
import org.jetbrains.annotations.ApiStatus

object ClientConfigurationNetworking {
	internal lateinit var connection: Connection

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