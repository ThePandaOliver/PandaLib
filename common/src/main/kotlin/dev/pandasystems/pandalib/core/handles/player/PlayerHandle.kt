package dev.pandasystems.pandalib.core.handles.player

import dev.pandasystems.pandalib.core.MinecraftRuntimeEnvironment
import dev.pandasystems.pandalib.core.lifecycles.ClientLifecycle
import dev.pandasystems.pandalib.core.lifecycles.ServerLifecycle
import dev.pandasystems.pandalib.core.minecraftRuntime
import net.minecraft.world.entity.player.Player
import java.util.UUID

/**
 * Represents a reference to a player.
 */
class PlayerHandle(
    val uuid: UUID,
    val resolve: (uuid: UUID) -> Player? = {
        if (minecraftRuntime.environment == MinecraftRuntimeEnvironment.SERVER) {
            val server = ServerLifecycle.serverInstance
            server?.playerList?.getPlayer(uuid)
        } else {
            val level = ClientLifecycle.clientInstance.level
            level?.getPlayerByUUID(uuid)
        }
    }
) {
    val isTracked: Boolean
        get() = resolve(uuid) != null

    val isOnline: Boolean
        get() = if (minecraftRuntime.environment == MinecraftRuntimeEnvironment.SERVER) {
            ServerLifecycle.serverInstance?.playerList?.getPlayer(uuid) != null
        } else {
            ClientLifecycle.clientInstance.connection?.getPlayerInfo(uuid) != null
        }
}

fun Player.handle(): PlayerHandle = PlayerHandle(uuid)