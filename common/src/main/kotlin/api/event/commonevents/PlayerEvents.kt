/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.event.commonevents

import dev.pandasystems.pandalib.api.event.CancellableEvent
import dev.pandasystems.pandalib.api.event.Event
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity

data class ServerPlayerJoinEvent(val player: ServerPlayer) : Event
data class ServerPlayerLeaveEvent(val player: ServerPlayer) : Event
data class ServerPlayerRespawnEvent(
	val oldPlayer: ServerPlayer, val newPlayer: ServerPlayer,
	val keepInventory: Boolean
) : Event

class ServerPlayerWorldChangeEvent {
	data class Pre(
		val player: ServerPlayer,
		val oldLevel: ServerLevel,
		val newLevel: ServerLevel
	) : CancellableEvent {
		override var cancelled: Boolean = false
	}

	data class Post(
		val player: ServerPlayer,
		val oldLevel: ServerLevel, val newLevel: ServerLevel
	) : Event
}