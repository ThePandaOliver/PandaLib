package dev.pandasystems.pandalib.core.player

import net.minecraft.world.entity.player.Player
import java.util.UUID

/**
 * Represents a reference to a player.
 */
class PlayerHandle(
    val uuid: UUID,
    val resolve: (uuid: UUID) -> Player
) {
}