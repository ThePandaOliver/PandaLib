/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ServerBlockEvents")

package dev.pandasystems.pandalib.event.server

import dev.pandasystems.pandalib.utils.cancelableEvent
import dev.pandasystems.pandalib.utils.event
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

@get:JvmName("breakPreEvent")
val serverBlockBreakPreEvent = cancelableEvent<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Boolean>()
@get:JvmName("breakPostEvent")
val serverBlockBreakPostEvent = event<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()

@get:JvmName("placePreEvent")
val serverBlockPlacePreEvent = cancelableEvent<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Boolean>()
@get:JvmName("placePostEvent")
val serverBlockPlacePostEvent = event<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()
