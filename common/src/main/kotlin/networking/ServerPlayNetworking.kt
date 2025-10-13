/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.networking.PacketSender
import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacket
import dev.pandasystems.pandalib.platform.game
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerChunkCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import org.jetbrains.annotations.ApiStatus

object ServerPlayNetworking {
	@JvmField
	internal val packetHandlers = mutableMapOf<CustomPacketPayload.Type<out CustomPacketPayload>, PlayPayloadHandler<CustomPacketPayload>>()


	// Packet Registration

	@JvmStatic
	fun <T : CustomPacketPayload> registerHandler(type: CustomPacketPayload.Type<T>, handler: PlayPayloadHandler<T>) {
		require(!packetHandlers.containsKey(type)) { "Packet type $type already has a handler" }
		@Suppress("UNCHECKED_CAST")
		packetHandlers[type] = handler as PlayPayloadHandler<CustomPacketPayload>
	}


	// Packet Sending

	@JvmStatic
	fun send(player: ServerPlayer, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = send(player, listOf(payload, *payloads))

	@JvmStatic
	fun send(player: ServerPlayer, payloads: Collection<CustomPacketPayload>) {
		player.connection.send(createPacket(*payloads.toTypedArray()))
	}

	@JvmStatic
	fun sendToAll(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) = sendToAll(listOf(payload, *payloads))

	@JvmStatic
	fun sendToAll(payloads: Collection<CustomPacketPayload>) {
		val server = requireNotNull(game.server) { "Cannot send clientbound payloads from the client" }
		server.playerList.broadcastAll(createPacket(*payloads.toTypedArray()))
	}

	@JvmStatic
	fun sendInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		sendInDimension(level, listOf(payload, *payloads))

	@JvmStatic
	fun sendInDimension(level: ServerLevel, payloads: Collection<CustomPacketPayload>) {
		val packet = createPacket(*payloads.toTypedArray())
		level.server.playerList.broadcastAll(packet, level.dimension())
	}

	@JvmStatic
	fun sendToNear(
		level: ServerLevel, excluded: ServerPlayer, x: Double, y: Double, z: Double,
		radius: Double, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload
	) = sendToNear(level, excluded, x, y, z, radius, listOf(payload, *payloads))

	@JvmStatic
	fun sendToNear(
		level: ServerLevel, excluded: ServerPlayer, x: Double, y: Double, z: Double,
		radius: Double, payloads: Collection<CustomPacketPayload>
	) {
		val packet = createPacket(*payloads.toTypedArray())
		level.server.playerList.broadcast(excluded, x, y, z, radius, level.dimension(), packet)
	}

	@JvmStatic
	fun sendToTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		sendToTrackingEntity(entity, listOf(payload, *payloads))

	@JvmStatic
	fun sendToTrackingEntity(entity: Entity, payloads: Collection<CustomPacketPayload>) {
		check(!entity.level().isClientSide()) { "Cannot send clientbound payloads on the client" }
		val chunkSource = entity.level().chunkSource
		if (chunkSource is ServerChunkCache) {
			chunkSource.broadcast(entity, createPacket(*payloads.toTypedArray()))
		}
		// Silently ignore custom Level implementations which may not return ServerChunkCache.
	}

	@JvmStatic
	fun sendToTrackingEntityAndSelf(entity: Entity, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) =
		sendToTrackingEntityAndSelf(entity, listOf(payload, *payloads))

	@JvmStatic
	fun sendToTrackingEntityAndSelf(entity: Entity, payloads: Collection<CustomPacketPayload>) {
		check(!entity.level().isClientSide()) { "Cannot send clientbound payloads on the client" }
		val chunkSource = entity.level().chunkSource
		if (chunkSource is ServerChunkCache) {
			chunkSource.broadcastAndSend(entity, createPacket(*payloads.toTypedArray()))
		}
		// Silently ignore custom Level implementations which may not return ServerChunkCache.
	}

	@JvmStatic
	fun sendToTrackingChunk(
		level: ServerLevel, chunkPos: ChunkPos,
		payload: CustomPacketPayload, vararg payloads: CustomPacketPayload
	) = sendToTrackingChunk(level, chunkPos, listOf(payload, *payloads))

	@JvmStatic
	fun sendToTrackingChunk(
		level: ServerLevel, chunkPos: ChunkPos,
		payloads: Collection<CustomPacketPayload>
	) {
		val packet: Packet<*> = createPacket(*payloads.toTypedArray())
		level.chunkSource.chunkMap.getPlayers(chunkPos, false).forEach { it.connection.send(packet) }
	}

	@JvmStatic
	fun createPacket(vararg payloads: CustomPacketPayload): Packet<*> {
		require(payloads.isNotEmpty()) { "Requires at least one payload" }
		return if (payloads.size > 1) {
			ClientboundBundlePacket(payloads.map(::ClientboundPLPayloadPacket))
		} else {
			ClientboundPLPayloadPacket(payloads.first())
		}
	}

	fun interface PlayPayloadHandler<T : CustomPacketPayload> {
		fun receive(payload: T, context: Context)
	}

	@ApiStatus.NonExtendable
	interface Context {
		fun server(): MinecraftServer
		fun player(): ServerPlayer
		fun responseSender(): PacketSender
	}
}