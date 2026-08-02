package dev.pandasystems.pandalib.networking

interface PacketContext {
    /**
     * If server side: Represents the sender of the packet
     * If client side: Represents the receiver of the packet
     */
    val peer: NetworkPeer

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class PacketContextImpl(
    override val peer: NetworkPeer,
    override val executor: NetworkExecutor,
    private val sender: PacketSender,
    private val replyToServer: Boolean,
) : PacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        if (replyToServer) sender.sendToServer(packet, value)
        else sender.sendToPeer(peer, packet, value)
    }
}


fun interface PacketHandler<T> {
    fun handle(context: PacketContext, packet: T)
}