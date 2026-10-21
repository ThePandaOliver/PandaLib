package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener

interface ConfigurationPacketContext {
    /**
     * The network configuration listener representing the connecting client.
     */
    val listener: ServerConfigurationPacketListener

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class ConfigurationPacketContextImpl(
    override val listener: ServerConfigurationPacketListener,
    override val executor: NetworkExecutor,
    private val sender: ConfigurationPacketSender,
) : ConfigurationPacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        sender.sendToClient(listener, packet, value)
    }
}

fun interface ConfigurationPacketHandler<T> {
    fun handle(context: ConfigurationPacketContext, packet: T)
}
