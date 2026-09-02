package dev.pandasystems.pandalib.event.events

import dev.pandasystems.pandalib.core.handles.player.PlayerHandle
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.eventCancelable
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

// --- Connection -------------------------------------------------------------------
data class ServerPlayerConnectionEventContext(val player: PlayerHandle)

val playerServerJoin by event<ServerPlayerConnectionEventContext>()
val playerServerLeave by event<ServerPlayerConnectionEventContext>()

// --- Respawn ----------------------------------------------------------------------
sealed interface ServerPlayerRespawnEventContext {
    val player: PlayerHandle

    val fabric: ServerPlayerRespawnEventContextFabric?
        get() = this as? ServerPlayerRespawnEventContextFabric
    val forge: ServerPlayerRespawnEventContextForge?
        get() = this as? ServerPlayerRespawnEventContextForge
}

data class ServerPlayerRespawnEventContextFabric(
    override val player: PlayerHandle,
    val oldPlayer: PlayerHandle,
    val alive: Boolean
) : ServerPlayerRespawnEventContext

data class ServerPlayerRespawnEventContextForge(
    override val player: PlayerHandle
) : ServerPlayerRespawnEventContext

val playerServerAfterRespawn by event<ServerPlayerRespawnEventContext>()

// --- Block ------------------------------------------------------------------------
data class ServerPlayerBlockBreakEventContext(
    val level: LevelAccessor,
    val player: PlayerHandle,
    val pos: BlockPos,
    val blockState: BlockState,
    val blockEntity: BlockEntity?
)

val playerBlockBreakBefore by eventCancelable<ServerPlayerBlockBreakEventContext>()
val playerBlockBreakAfter by event<ServerPlayerBlockBreakEventContext>()
val playerBlockBreakCanceled by event<ServerPlayerBlockBreakEventContext>()