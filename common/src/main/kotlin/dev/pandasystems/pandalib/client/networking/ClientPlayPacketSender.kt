package dev.pandasystems.pandalib.client.networking

import dev.pandasystems.pandalib.networking.PacketType

interface ClientPlayPacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)
}
