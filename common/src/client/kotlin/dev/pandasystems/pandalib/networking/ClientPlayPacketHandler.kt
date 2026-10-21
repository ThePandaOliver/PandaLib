package dev.pandasystems.pandalib.networking

import net.minecraft.world.entity.player.Player

interface ClientPlayPacketContext {
    /**
     * Represents the local client player receiving the packet.
     */
    val player: Player

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class ClientPlayPacketContextImpl(
    override val player: Player,
    override val executor: NetworkExecutor,
    private val sender: ClientPlayPacketSender,
) : ClientPlayPacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        sender.sendToServer(packet, value)
    }
}

fun interface ClientPlayPacketHandler<T> {
    fun handle(context: ClientPlayPacketContext, packet: T)
}
