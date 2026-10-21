package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener

interface ConfigurationPacketSender {
    fun <T> sendToClient(
        listener: ServerConfigurationPacketListener,
        type: PacketType<T>,
        value: T,
    )
}
