package dev.pandasystems.pandalib.networking

interface NetworkRegistrar {
    fun <T> register(
        type: PacketType<T>,
        handler: PacketHandler<T>,
    )
}