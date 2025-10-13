/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ClientBlockEvents")

package dev.pandasystems.pandalib.event.client

import dev.pandasystems.pandalib.utils.event
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

@get:JvmName("breakEvent")
val clientBlockBreakEvent = event<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>() // TODO: invoke client event
@get:JvmName("placeEvent")
val clientBlockPlaceEvent = event<(level: Level, pos: BlockPos, state: BlockState, entity: Entity?) -> Unit>()