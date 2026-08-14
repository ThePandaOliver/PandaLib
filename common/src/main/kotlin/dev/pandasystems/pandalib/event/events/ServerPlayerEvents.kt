package dev.pandasystems.pandalib.event.events

import dev.pandasystems.pandalib.core.handles.player.PlayerHandle
import dev.pandasystems.pandalib.event.event1
import dev.pandasystems.pandalib.event.event3
import dev.pandasystems.pandalib.event.event4
import dev.pandasystems.pandalib.event.event5
import dev.pandasystems.pandalib.event.eventCancelable4
import dev.pandasystems.pandalib.event.eventCancelable5
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

val playerServerJoin = event1<PlayerHandle>()
val playerServerLeave = event1<PlayerHandle>()
val playerServerAfterRespawn = event3<PlayerHandle, PlayerHandle, Boolean>()

val playerBlockBreakBefore = eventCancelable5<Level, PlayerHandle, BlockPos, BlockState, BlockEntity>()
val playerBlockBreakAfter = event5<Level, PlayerHandle, BlockPos, BlockState, BlockEntity>()
val playerBlockBreakCanceled = event5<Level, PlayerHandle, BlockPos, BlockState, BlockEntity>()