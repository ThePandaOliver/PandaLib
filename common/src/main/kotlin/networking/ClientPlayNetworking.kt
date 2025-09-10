/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.interfaces.PacketSender
import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacket
import dev.pandasystems.pandalib.networking.packets.bundle.ServerboundPLBundlePacket
import dev.pandasystems.pandalib.platform.game
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus

object ClientPlayNetworking {
	@JvmField
	internal val packetHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PlayPayloadHandler<CustomPacketPayload>>()


	// Packet Registration

	@JvmStatic
	fun <T : CustomPacketPayload> registerHandler(
		type: CustomPacketPayload.Type<T>, handler: PlayPayloadHandler<T>
	) {
		require(!packetHandlers.containsKey(type)) { "Packet type $type already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[type] = handler as PlayPayloadHandler<CustomPacketPayload>
	}


	// Packet Sending

	@JvmStatic
	fun send(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
		require(game.isClient) { "Cannot send serverbound payloads from the server" }
		val listener = requireNotNull(Minecraft.getInstance().player!!.connection)
		listener.send(makePacket(payload, *payloads))
	}

	private fun makePacket(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload): Packet<*> {
		if (payloads.isNotEmpty()) {
			val packets = mutableListOf<Packet<in ServerGamePacketListener>>()
			packets.add(ServerboundPLPayloadPacket(payload))
			payloads.forEach { packets.add(ServerboundPLPayloadPacket(it)) }
			return ServerboundPLBundlePacket(packets)
		} else {
			return ServerboundPLPayloadPacket(payload)
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