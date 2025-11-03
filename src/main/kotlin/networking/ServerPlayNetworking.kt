/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
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
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.Connection
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketSendListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerChunkCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos

object ServerPlayNetworking {
	internal val packetHandlers = mutableMapOf<ResourceLocation, PlayPayloadHandler<CustomPacketPayload>>()


	// Packet Registration

	fun <T : CustomPacketPayload> registerHandler(resourceLocation: ResourceLocation, handler: PlayPayloadHandler<T>) {
		require(!packetHandlers.containsKey(resourceLocation)) { "Packet type $resourceLocation already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[resourceLocation] = handler as PlayPayloadHandler<CustomPacketPayload>
	}


	// Packet Sending

	fun send(player: ServerPlayer, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(player, listOf(payload, *payloads))

	fun send(player: ServerPlayer, payloads: Collection<CustomPacketPayload>) {
		payloads.forEach { player.connection.send(createPacket(it)) }
	}

	fun sendToAll(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = sendToAll(listOf(payload, *payloads))

	fun sendToAll(payloads: Collection<CustomPacketPayload>) {
		payloads.forEach {
			val server = requireNotNull(gameEnvironment.server) { "Cannot send clientbound payloads from the client" }
			server.playerList.broadcastAll(createPacket(it))
		}
	}

	fun sendInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		sendInDimension(level, listOf(payload, *payloads))

	fun sendInDimension(level: ServerLevel, payloads: Collection<CustomPacketPayload>) {
		payloads.forEach {
			val packet = createPacket(it)
			level.server.playerList.broadcastAll(packet, level.dimension())
		}
	}

	fun sendToNear(
		level: ServerLevel, excluded: ServerPlayer, x: Double, y: Double, z: Double,
		radius: Double, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload
	) = sendToNear(level, excluded, x, y, z, radius, listOf(payload, *payloads))

	fun sendToNear(
		level: ServerLevel, excluded: ServerPlayer, x: Double, y: Double, z: Double,
		radius: Double, payloads: Collection<CustomPacketPayload>
	) {
		payloads.forEach {
			val packet = createPacket(it)
			level.server.playerList.broadcast(excluded, x, y, z, radius, level.dimension(), packet)
		}
	}

	fun sendToTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		sendToTrackingEntity(entity, listOf(payload, *payloads))

	fun sendToTrackingEntity(entity: Entity, payloads: Collection<CustomPacketPayload>) {
		check(!entity.level().isClientSide) { "Cannot send clientbound payloads on the client" }
		val chunkSource = entity.level().chunkSource
		if (chunkSource is ServerChunkCache) {
			payloads.forEach { chunkSource.broadcast(entity, createPacket(it)) }
		}
		// Silently ignore custom Level implementations which may not return ServerChunkCache.
	}

	fun sendToTrackingEntityAndSelf(entity: Entity, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		sendToTrackingEntityAndSelf(entity, listOf(payload, *payloads))

	fun sendToTrackingEntityAndSelf(entity: Entity, payloads: Collection<CustomPacketPayload>) {
		check(!entity.level().isClientSide) { "Cannot send clientbound payloads on the client" }
		val chunkSource = entity.level().chunkSource
		if (chunkSource is ServerChunkCache) {
			payloads.forEach { chunkSource.broadcastAndSend(entity, createPacket(it)) }
		}
		// Silently ignore custom Level implementations which may not return ServerChunkCache.
	}

	fun sendToTrackingChunk(
		level: ServerLevel, chunkPos: ChunkPos,
		payload: CustomPacketPayload, vararg payloads: CustomPacketPayload
	) = sendToTrackingChunk(level, chunkPos, listOf(payload, *payloads))

	fun sendToTrackingChunk(
		level: ServerLevel, chunkPos: ChunkPos,
		payloads: Collection<CustomPacketPayload>
	) {
		payloads.forEach { payload ->
			val packet: Packet<*> = createPacket(payload)
			level.chunkSource.chunkMap.getPlayers(chunkPos, false).forEach { it.connection.send(packet) }
		}
	}

	fun createPacket(payload: CustomPacketPayload): Packet<*> {
		val friendlyByteBuf = FriendlyByteBuf(Unpooled.buffer())
		payload.write(friendlyByteBuf)
		return ClientboundCustomPayloadPacket(payload.id(), friendlyByteBuf)
	}


	// Packet Receiving

	fun handlePayload(handler: ServerGamePacketListener, payload: CustomPacketPayload) {
		class Sender(val connection: Connection): PacketSender {
			override fun createPacket(payload: CustomPacketPayload): Packet<*> {
				return createPacket(payload)
			}

			override fun sendPacket(callback: PacketSendListener?, packet: Packet<*>) {
				connection.send(packet, callback)
			}

			override fun disconnect(disconnectReason: Component) {
				connection.disconnect(disconnectReason)
			}
		}

		when (handler) {
			is ServerGamePacketListenerImpl -> {
				val context = object : Context {
					override fun server(): MinecraftServer = handler.server
					override fun player(): ServerPlayer = handler.player
					override fun responseSender(): PacketSender = Sender(handler.connection)
				}
				packetHandlers[payload.id()]?.receive(payload, context)
			}
		}
	}

	fun interface PlayPayloadHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	interface Context {
		fun server(): MinecraftServer
		fun player(): ServerPlayer
		fun responseSender(): PacketSender
	}
}