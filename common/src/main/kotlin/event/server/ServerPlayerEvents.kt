/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ServerPlayerEvents")

package dev.pandasystems.pandalib.event.server

import dev.pandasystems.pandalib.utils.cancelableEvent
import dev.pandasystems.pandalib.utils.event
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity

val serverPlayerJoinEvent = event<(player: ServerPlayer) -> Unit>()
val serverPlayerLeaveEvent = event<(player: ServerPlayer) -> Unit>()
val serverPlayerRespawnEvent = event<(
	oldPlayer: ServerPlayer, newPlayer: ServerPlayer,
	keepInventory: Boolean, removalReason: Entity.RemovalReason
) -> Unit>()

val serverPlayerChangeDimensionPreEvent = cancelableEvent<(
	player: ServerPlayer, oldLevel: ServerLevel,
	newLevel: ServerLevel
) -> Boolean>()
val serverPlayerChangeDimensionPostEvent = event<(
	player: ServerPlayer, oldLevel: ServerLevel,
	newLevel: ServerLevel
) -> Unit>()
