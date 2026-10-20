package dev.pandasystems.pandalib.networking

import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener

interface ConfigurationPacketContext {
    /**
     * The network configuration listener representing the player, or null if handled on client side.
     */
    val listener: ServerConfigurationPacketListener?

    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class ConfigurationPacketContextImpl(
    override val listener: ServerConfigurationPacketListener?,
    override val executor: NetworkExecutor,
    private val sender: ConfigurationPacketSender,
    private val replyToServer: Boolean,
) : ConfigurationPacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        if (replyToServer) {
            sender.sendToServer(packet, value)
        } else {
            val serverListener = checkNotNull(listener) {
                "Cannot reply to client without a valid ServerConfigurationPacketListenerImpl."
            }
            sender.sendToClient(serverListener, packet, value)
        }
    }
}

fun interface ConfigurationPacketHandler<T> {
    fun handle(context: ConfigurationPacketContext, packet: T)
}
