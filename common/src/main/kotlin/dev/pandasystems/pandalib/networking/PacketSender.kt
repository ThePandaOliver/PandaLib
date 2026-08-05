package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.core.handles.player.PlayerHandle

interface PacketSender {
    fun <T> sendToServer(type: PacketType<T>, value: T)

    fun <T> sendToPeer(peer: PlayerHandle, type: PacketType<T>, value: T)

    fun <T> broadcast(
        type: PacketType<T>,
        value: T,
        filter: (PlayerHandle) -> Boolean = { true },
    )
}