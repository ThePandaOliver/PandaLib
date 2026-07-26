package dev.pandasystems.pandalib.networking

/** Registers packet handlers and sends registered packets. */
interface NetworkRegistrar : PacketSender {
    fun <T> register(
        type: PacketType<T>,
        handler: PacketHandler<T>,
    )
}
