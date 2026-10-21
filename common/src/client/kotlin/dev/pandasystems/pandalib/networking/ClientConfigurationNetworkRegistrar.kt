package dev.pandasystems.pandalib.networking

interface ClientConfigurationNetworkRegistrar {
    fun <T> register(
        type: PacketType<T>,
        handler: ClientConfigurationPacketHandler<T>,
    )
}
