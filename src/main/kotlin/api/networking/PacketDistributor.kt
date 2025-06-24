/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("PacketDistributor")

package dev.pandasystems.pandalib.api.networking

import dev.pandasystems.pandalib.api.platform.game
import dev.pandasystems.pandalib.api.networking.packets.ServerboundBundlePacket
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.level.ServerChunkCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos

/*
 * The following code were copied from the NeoForge project and converted to Kotlin by Oliver Froberg
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 *
 * Original source: https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/network/PacketDistributor.java
 */

fun sendPacketToServer(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
	require(game.isClient) { "Cannot send serverbound payloads from the server" }
	val listener = requireNotNull(Minecraft.getInstance().player!!.connection)
	listener.send(makeServerboundPacket(payload, *payloads))
}

fun sendPacketToPlayer(player: ServerPlayer, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
	player.connection.send(makeClientboundPacket(payload, *payloads))
}

fun sendPacketToPlayersInDimension(level: ServerLevel, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
	val packet = makeClientboundPacket(payload, *payloads)
	level.server.playerList.broadcastAll(packet, level.dimension())
}

fun sendPacketToPlayersNear(
	level: ServerLevel, excluded: ServerPlayer, x: Double, y: Double, z: Double,
	radius: Double, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload
) {
	val packet = makeClientboundPacket(payload, *payloads)
	level.server.playerList.broadcast(excluded, x, y, z, radius, level.dimension(), packet)
}

fun sendPacketToAllPlayers(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
	val server = requireNotNull(game.server) { "Cannot send clientbound payloads from the client" }
	server.playerList.broadcastAll(makeClientboundPacket(payload, *payloads))
}

fun sendPacketToPlayersTrackingEntity(entity: Entity, payload: CustomPacketPayload, vararg payloads: CustomPacketPayload) {
	check(!entity.level().isClientSide()) { "Cannot send clientbound payloads on the client" }
	val chunkSource = entity.level().chunkSource
	if (chunkSource is ServerChunkCache) {
		chunkSource.broadcast(entity, makeClientboundPacket(payload, *payloads))
	}
	// Silently ignore custom Level implementations which may not return ServerChunkCache.
}

fun sendPacketToPlayersTrackingEntityAndSelf(
	entity: Entity, payload: CustomPacketPayload,
	vararg payloads: CustomPacketPayload
) {
	check(!entity.level().isClientSide()) { "Cannot send clientbound payloads on the client" }
	val chunkSource = entity.level().chunkSource
	if (chunkSource is ServerChunkCache) {
		chunkSource.broadcastAndSend(entity, makeClientboundPacket(payload, *payloads))
	}
	// Silently ignore custom Level implementations which may not return ServerChunkCache.
}

fun sendPacketToPlayersTrackingChunk(
	level: ServerLevel, chunkPos: ChunkPos,
	payload: CustomPacketPayload, vararg payloads: CustomPacketPayload
) {
	val packet: Packet<*> = makeClientboundPacket(payload, *payloads)
	level.chunkSource.chunkMap.getPlayers(chunkPos, false).forEach { it.connection.send(packet) }
}

private fun makeServerboundPacket(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload): Packet<*> {
	if (payloads.isNotEmpty()) {
		val packets = mutableListOf<Packet<in ServerGamePacketListener>>()
		packets.add(ServerboundCustomPayloadPacket(payload))
		payloads.forEach { packets.add(ServerboundCustomPayloadPacket(it)) }
		return ServerboundBundlePacket(packets)
	} else {
		return ServerboundCustomPayloadPacket(payload)
	}
}

private fun makeClientboundPacket(payload: CustomPacketPayload, vararg payloads: CustomPacketPayload): Packet<*> {
	if (payloads.isNotEmpty()) {
		val packets = mutableListOf<Packet<in ClientGamePacketListener>>()
		packets.add(ClientboundCustomPayloadPacket(payload))
		payloads.forEach { packets.add(ClientboundCustomPayloadPacket(it)) }
		return ClientboundBundlePacket(packets)
	} else {
		return ClientboundCustomPayloadPacket(payload)
	}
}