package dev.pandasystems.pandalib.networking.minecraft

import dev.pandasystems.pandalib.networking.NetworkPeer
import net.minecraft.world.entity.player.Player

data class MinecraftNetworkPeer(
    override val id: String
) : NetworkPeer {
    constructor(player: Player) : this(player.stringUUID)
}