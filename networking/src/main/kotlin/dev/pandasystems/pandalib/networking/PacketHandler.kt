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

fun interface PacketHandler<T> {
    fun handle(context: PacketContext, packet: T)
}