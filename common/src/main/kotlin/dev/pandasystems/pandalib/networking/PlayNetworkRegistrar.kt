package dev.pandasystems.pandalib.networking

interface PlayNetworkRegistrar {
    fun <T> register(
        type: PacketType<T>,
        handler: PlayPacketHandler<T>,
    )
}