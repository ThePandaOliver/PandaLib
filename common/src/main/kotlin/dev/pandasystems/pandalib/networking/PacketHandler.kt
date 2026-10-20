package dev.pandasystems.pandalib.networking

import net.minecraft.world.entity.player.Player

interface PacketContext {
    /**
     * Represents the sender if server side, or receiver if client side.
     */
    val peer: Player

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class PacketContextImpl(
    override val peer: Player,
    override val executor: NetworkExecutor,
    private val sender: PacketSender,
    private val replyToServer: Boolean,
) : PacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        if (replyToServer) sender.sendToServer(packet, value)
        else sender.sendToPlayer(peer, packet, value)
    }
}

fun interface PacketHandler<T> {
    fun handle(context: PacketContext, packet: T)
}
