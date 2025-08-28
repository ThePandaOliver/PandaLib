/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ServerPlayerEvents")

package dev.pandasystems.pandalib.event.serverevents

import dev.pandasystems.pandalib.listener.ListenerFactory
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.portal.TeleportTransition

val serverPlayerJoinEvent = ListenerFactory.create<(player: ServerPlayer) -> Unit>()
val serverPlayerLeaveEvent = ListenerFactory.create<(player: ServerPlayer) -> Unit>()
val serverPlayerRespawnEvent = ListenerFactory.create<(
	oldPlayer: ServerPlayer, newPlayer: ServerPlayer,
	keepInventory: Boolean, removalReason: Entity.RemovalReason
) -> Unit>()

val serverPlayerChangeDimensionPreEvent = ListenerFactory.createCancellable<(
	player: ServerPlayer, oldLevel: ServerLevel,
	newLevel: ServerLevel, teleportTransition: TeleportTransition
) -> Boolean>()
val serverPlayerChangeDimensionPostEvent = ListenerFactory.create<(
	player: ServerPlayer, oldLevel: ServerLevel,
	newLevel: ServerLevel, teleportTransition: TeleportTransition
) -> Unit>()