package dev.pandasystems.pandalib.networking.minecraft

import dev.pandasystems.pandalib.networking.NetworkPeer
import net.minecraft.world.entity.player.Player

class MinecraftNetworkPeer(val player: Player) : NetworkPeer {
    override val id: String
        get() = player.uuid.toString()
}