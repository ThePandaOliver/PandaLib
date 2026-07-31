package dev.pandasystems.pandalib.networking

interface PacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)

    fun <T> sendToPeer(peer: NetworkPeer, type: PacketType<T>, value: T)

    fun <T> broadcast(
        type: PacketType<T>,
        value: T,
        filter: (NetworkPeer) -> Boolean = { true },
    )
}