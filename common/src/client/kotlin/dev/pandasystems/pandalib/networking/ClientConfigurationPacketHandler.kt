package dev.pandasystems.pandalib.networking

interface ClientConfigurationPacketContext {
    val executor: NetworkExecutor

    fun <T> reply(packet: PacketType<T>, value: T)
}

class ClientConfigurationPacketContextImpl(
    override val executor: NetworkExecutor,
    private val sender: ClientConfigurationPacketSender,
) : ClientConfigurationPacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        sender.sendToServer(packet, value)
    }
}

fun interface ClientConfigurationPacketHandler<T> {
    fun handle(context: ClientConfigurationPacketContext, packet: T)
}
