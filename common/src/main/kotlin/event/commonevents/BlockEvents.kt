/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.event.commonevents

import dev.pandasystems.pandalib.event.CancellableEvent
import dev.pandasystems.pandalib.event.Event
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class BlockBreakEvent {
	class Pre(level: Level, blockPos: BlockPos, entity: Entity?) : CancellableBlockEvent(level, blockPos, entity)
	class Post(level: Level, blockPos: BlockPos, entity: Entity?) : BlockEvent(level, blockPos, entity)
}
class BlockPlaceEvent {
	class Pre(level: Level, blockPos: BlockPos, entity: Entity?) : CancellableBlockEvent(level, blockPos, entity)
	class Post(level: Level, blockPos: BlockPos, entity: Entity?) : BlockEvent(level, blockPos, entity)
}

abstract class BlockEvent(
	val level: Level,
	val blockPos: BlockPos,
	val entity: Entity?
) : Event {
	val blockState: BlockState get() = level.getBlockState(blockPos)
}

abstract class CancellableBlockEvent(level: Level, blockPos: BlockPos, entity: Entity?) : BlockEvent(level, blockPos, entity), CancellableEvent {
	override var cancelled: Boolean = false
}