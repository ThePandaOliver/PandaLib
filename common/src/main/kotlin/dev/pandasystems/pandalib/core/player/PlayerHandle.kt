package dev.pandasystems.pandalib.core.player

import dev.pandasystems.pandalib.core.GameLifecycle
import net.minecraft.world.entity.player.Player
import java.util.UUID

/**
 * Represents a reference to a player.
 */
sealed class PlayerHandle(
    val uuid: UUID,
    val resolve: (uuid: UUID) -> Player
) {
}

class ServerPlayerHandle(
    uuid: UUID,
) : PlayerHandle(
    uuid,
    resolve = {
        val server = GameLifecycle.serverInstance ?: throw IllegalStateException("Server not found")
        server.playerList.getPlayer(uuid) ?: throw IllegalStateException("Player not found")
    }
)

class ClientPlayerHandle(
    uuid: UUID,
) : PlayerHandle(
    uuid,
    resolve = {
        val level = GameLifecycle.clientInstance.level ?: throw IllegalStateException("Level not found")
        level.getPlayerByUUID(uuid) ?: throw IllegalStateException("Player not found")
    }
)