package dev.pandasystems.pandalib.client.networking

import dev.pandasystems.pandalib.networking.PacketType

interface ClientConfigurationPacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)
}
