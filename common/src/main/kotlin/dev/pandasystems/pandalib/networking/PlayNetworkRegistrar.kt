package dev.pandasystems.pandalib.networking

interface PlayNetworkRegistrar {
    fun <T> registerPayload(type: PacketType<T>)

    fun <T> register(
        type: PacketType<T>,
        handler: PlayPacketHandler<T>,
    )
}
