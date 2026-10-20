package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

interface ConfigurationPacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)

    fun <T> sendToClient(
        listener: ServerConfigurationPacketListener,
        type: PacketType<T>,
        value: T,
    )
}
