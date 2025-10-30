/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.event.server

import dev.pandasystems.pandalib.utils.cancelableEvent
import dev.pandasystems.pandalib.utils.event
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.portal.TeleportTransition

val serverPlayerJoinEvent = event<(player: ServerPlayer) -> Unit>()
val serverPlayerLeaveEvent = event<(player: ServerPlayer) -> Unit>()
val serverPlayerRespawnEvent = event<(
	oldPlayer: ServerPlayer, newPlayer: ServerPlayer,
	keepInventory: Boolean, removalReason: Entity.RemovalReason
) -> Unit>()

val serverPlayerChangeDimensionPreEvent = cancelableEvent<(
	player: ServerPlayer, oldLevel: ServerLevel,
	newLevel: ServerLevel, teleportTransition: TeleportTransition
) -> Boolean>()
val serverPlayerChangeDimensionPostEvent = event<(
	player: ServerPlayer, oldLevel: ServerLevel,
	newLevel: ServerLevel, teleportTransition: TeleportTransition
) -> Unit>()
