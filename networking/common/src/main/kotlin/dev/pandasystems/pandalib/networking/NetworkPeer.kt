package dev.pandasystems.pandalib.networking

import net.minecraft.world.entity.player.Player
import java.util.UUID

interface NetworkPeer {
    /**
     * The unique identifier of the peer.
     * Should represent the player's unique identifier.
     */
    val id: UUID
}

data class PlayerNetworkPeer(
    override val id: UUID
) : NetworkPeer {
    constructor(player: Player) : this(player.uuid)
}