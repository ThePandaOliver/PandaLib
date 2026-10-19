package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.core.utils.loadService
import dev.pandasystems.pandalib.event.Event
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

data class ServerConfigurationConnectionEventContext(
    val listener: ServerConfigurationPacketListenerImpl
)

interface ServerConfigurationEvents {
    val configurationStart: Event<ServerConfigurationConnectionEventContext>

    companion object : ServerConfigurationEvents by loadService()
}