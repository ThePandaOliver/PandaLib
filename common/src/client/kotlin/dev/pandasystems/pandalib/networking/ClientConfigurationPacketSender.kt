package dev.pandasystems.pandalib.networking

interface ClientConfigurationPacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)
}
