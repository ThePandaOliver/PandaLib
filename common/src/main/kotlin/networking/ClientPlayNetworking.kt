/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacket
import dev.pandasystems.pandalib.networking.packets.bundle.ServerboundPLBundlePacket
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.jetbrains.annotations.ApiStatus

object ClientPlayNetworking {
	internal val packetHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PlayPayloadHandler<CustomPacketPayload>>()


	// Packet Registration

	fun <T : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<T>, handler: PlayPayloadHandler<T>) {
		require(!packetHandlers.containsKey(type)) { "Packet type $type already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[type] = handler as PlayPayloadHandler<CustomPacketPayload>
	}


	// Packet Sending

	fun send(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(listOf(payload, *payloads))

	fun send(payloads: Collection<CustomPacketPayload>) {
		require(gameEnvironment.isClient) { "Cannot send serverbound payloads from the server" }
		val listener = requireNotNull(Minecraft.getInstance().player!!.connection)
		listener.send(createPacket(*payloads.toTypedArray()))
	}

	fun createPacket(vararg payloads: CustomPacketPayload): Packet<*> {
		require(payloads.isNotEmpty()) { "Requires at least one payload" }
		return if (payloads.size > 1) {
			ServerboundPLBundlePacket(payloads.map(::ServerboundPLPayloadPacket))
		} else {
			ServerboundPLPayloadPacket(payloads.first())
		}
	}

	fun interface PlayPayloadHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	@ApiStatus.NonExtendable
	interface Context {
		fun client(): Minecraft
		fun player(): LocalPlayer
		fun responseSender(): PacketSender
	}
}