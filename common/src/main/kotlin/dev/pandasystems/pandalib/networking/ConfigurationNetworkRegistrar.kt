package dev.pandasystems.pandalib.networking

interface ConfigurationNetworkRegistrar {
    fun <T> register(
        type: PacketType<T>,
        handler: ConfigurationPacketHandler<T>,
    )
}
