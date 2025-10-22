/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
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
