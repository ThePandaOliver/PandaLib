package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

interface ConfigurationPacketSender {
    /**
     * Sends a configuration packet from the client to the server.
     */
    fun <T> sendToServer(type: PacketType<T>, value: T)

    /**
     * Sends a configuration packet from the server to a specific client during the configuration phase.
     */
    fun <T> sendToClient(
        listener: ServerConfigurationPacketListener,
        type: PacketType<T>,
        value: T,
    )
}
