package dev.pandasystems.pandalib.networking

interface PacketContext {
    val peer: NetworkPeer
    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

fun interface PacketHandler<T> {
    fun handle(context: PacketContext, packet: T)
}