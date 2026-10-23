package dev.pandasystems.pandalib.client.networking

import dev.pandasystems.pandalib.networking.PacketType

interface ClientPlayNetworkRegistrar {
    fun <T> register(
	    type: PacketType<T>,
	    handler: ClientPlayPacketHandler<T>,
    )
}
