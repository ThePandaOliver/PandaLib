/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("BlockEvents")

package dev.pandasystems.pandalib.event.serverevents

import dev.pandasystems.pandalib.listener.ListenerFactory
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

val blockBreakPreEvent = ListenerFactory.createCancellable<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Boolean>()
val blockBreakPostEvent = ListenerFactory.create<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()

val blockPlacePreEvent = ListenerFactory.createCancellable<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Boolean>()
val blockPlacePostEvent = ListenerFactory.create<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()