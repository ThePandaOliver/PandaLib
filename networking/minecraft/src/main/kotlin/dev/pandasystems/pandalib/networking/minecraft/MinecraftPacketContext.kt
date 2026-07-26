package dev.pandasystems.pandalib.networking.minecraft

import dev.pandasystems.pandalib.networking.NetworkExecutor
import dev.pandasystems.pandalib.networking.NetworkPeer
import dev.pandasystems.pandalib.networking.PacketContext
import dev.pandasystems.pandalib.networking.PacketSender
import dev.pandasystems.pandalib.networking.PacketType

class MinecraftPacketContext(
    override val peer: NetworkPeer,
    override val executor: NetworkExecutor,
    private val sender: PacketSender,
    private val replyToServer: Boolean,
) : PacketContext {
    override fun <T> reply(packet: PacketType<T>, value: T) {
        if (replyToServer) sender.sendToServer(packet, value)
        else sender.sendToPeer(peer, packet, value)
    }
}
