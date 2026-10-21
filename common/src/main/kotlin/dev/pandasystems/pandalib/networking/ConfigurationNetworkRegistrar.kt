package dev.pandasystems.pandalib.networking

interface ConfigurationNetworkRegistrar {
    fun <T> registerPayload(type: PacketType<T>)

    fun <T> register(
        type: PacketType<T>,
        handler: ConfigurationPacketHandler<T>,
    )
}
