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
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

val serverBlockBreakPreEvent = cancelableEvent<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Boolean>()
val serverBlockBreakPostEvent = event<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()

val serverBlockPlacePreEvent = cancelableEvent<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Boolean>()
val serverBlockPlacePostEvent = event<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()
