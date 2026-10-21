package dev.pandasystems.pandalib.networking

interface ClientPlayNetworkRegistrar {
    fun <T> register(
        type: PacketType<T>,
        handler: ClientPlayPacketHandler<T>,
    )
}
