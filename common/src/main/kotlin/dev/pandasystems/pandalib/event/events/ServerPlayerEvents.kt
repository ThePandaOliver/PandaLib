package dev.pandasystems.pandalib.event.events

import dev.pandasystems.pandalib.core.handles.player.PlayerHandle
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.eventCancelable
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

data class ServerPlayerConnectionEventContext(val player: PlayerHandle)

val playerServerJoin by event<ServerPlayerConnectionEventContext>()
val playerServerLeave by event<ServerPlayerConnectionEventContext>()

data class ServerPlayerRespawnEventContext(val oldPlayer: PlayerHandle, val newPlayer: PlayerHandle, val alive: Boolean)
val playerServerAfterRespawn by event<ServerPlayerRespawnEventContext>()

data class ServerPlayerBlockBreakEventContext(
    val level: Level,
    val player: PlayerHandle,
    val pos: BlockPos,
    val state: BlockState,
    val blockEntity: BlockEntity?
)

val playerBlockBreakBefore by eventCancelable<ServerPlayerBlockBreakEventContext>()
val playerBlockBreakAfter by event<ServerPlayerBlockBreakEventContext>()
val playerBlockBreakCanceled by event<ServerPlayerBlockBreakEventContext>()