package dev.pandasystems.pandalib.networking

import net.minecraft.world.entity.player.Player

interface PlayPacketContext {
    /**
     * Represents the player who sent the packet to the server.
     */
    val player: Player

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class PlayPacketContextImpl(
    override val player: Player,
    override val executor: NetworkExecutor,
    private val sender: PlayPacketSender,
) : PlayPacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        sender.sendToPlayer(player, packet, value)
    }
}

fun interface PlayPacketHandler<T> {
    fun handle(context: PlayPacketContext, packet: T)
}
