package dev.pandasystems.pandalib.networking

import net.minecraft.world.entity.player.Player

interface PlayPacketContext {
    /**
     * Represents the sender if server side, or receiver if client side.
     */
    val player: Player

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class PlayPacketContextImpl(
	override val player: Player,
	override val executor: NetworkExecutor,
	private val sender: PlayPacketSender,
	private val replyToServer: Boolean,
) : PlayPacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        if (replyToServer) sender.sendToServer(packet, value)
        else sender.sendToPlayer(player, packet, value)
    }
}

fun interface PlayPacketHandler<T> {
    fun handle(context: PlayPacketContext, packet: T)
}
