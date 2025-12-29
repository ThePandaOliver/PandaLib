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

package dev.pandasystems.pandalib.event.client

import dev.pandasystems.pandalib.utils.event
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

val clientBlockBreakEvent = event<(level: Level, pos: BlockPos, state: BlockState, player: LocalPlayer) -> Unit>()
val clientBlockPlaceEvent = event<(level: Level, pos: BlockPos, state: BlockState, player: LocalPlayer) -> Unit>()