package dev.pandasystems.pandalib.networking

interface PacketSender {
    /** Sends a [PacketDirection.CLIENT_TO_SERVER] packet to the connected server. */
    fun <T> sendToServer(
        type: PacketType<T>,
        value: T,
    )

    /** Sends a [PacketDirection.SERVER_TO_CLIENT] packet to one peer. */
    fun <T> sendToPeer(
        peer: NetworkPeer,
        type: PacketType<T>,
        value: T,
    )

    /** Sends a [PacketDirection.SERVER_TO_CLIENT] packet to every matching peer. */
    fun <T> broadcast(
        type: PacketType<T>,
        value: T,
        filter: (NetworkPeer) -> Boolean = { true },
    )
}
