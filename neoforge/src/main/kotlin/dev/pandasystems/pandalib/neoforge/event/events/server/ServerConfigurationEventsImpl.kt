package dev.pandasystems.pandalib.neoforge.event.events.server

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.event.Event
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationConnectionEventContext
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationEvents
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent

@AutoService(ServerConfigurationEvents::class)
class ServerConfigurationEventsImpl : ServerConfigurationEvents {
    override val configurationStart: Event<ServerConfigurationConnectionEventContext> = event()

    internal fun onRegisterConfigurationTasks(event: RegisterConfigurationTasksEvent) {
        val listener = event.listener
        if (listener is ServerConfigurationPacketListenerImpl) {
            configurationStart(ServerConfigurationConnectionEventContext(listener))
        }
    }
}