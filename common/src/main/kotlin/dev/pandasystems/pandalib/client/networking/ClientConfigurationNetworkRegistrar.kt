package dev.pandasystems.pandalib.client.networking

import dev.pandasystems.pandalib.networking.PacketType

interface ClientConfigurationNetworkRegistrar {
    fun <T> register(
	    type: PacketType<T>,
	    handler: ClientConfigurationPacketHandler<T>,
    )
}
