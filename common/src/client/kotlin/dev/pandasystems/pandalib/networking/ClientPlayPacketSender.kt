package dev.pandasystems.pandalib.networking

interface ClientPlayPacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)
}
