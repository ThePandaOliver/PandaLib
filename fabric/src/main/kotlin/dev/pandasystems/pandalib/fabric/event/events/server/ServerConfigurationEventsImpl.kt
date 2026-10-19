package dev.pandasystems.pandalib.fabric.event.events.server

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.event.Event
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationConnectionEventContext
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationEvents
import dev.pandasystems.pandalib.fabric.event.bindEvent
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents

@AutoService(ServerConfigurationEvents::class)
class ServerConfigurationEventsImpl : ServerConfigurationEvents {
    override val configurationStart: Event<ServerConfigurationConnectionEventContext> =
        ServerConfigurationConnectionEvents.CONFIGURE.bindEvent(
            createListener = { subInvoker ->
                ServerConfigurationConnectionEvents.Configure { handler, _ ->
                    subInvoker(ServerConfigurationConnectionEventContext(handler))
                }
            },
            onInvoke = { _, _ -> }
        )
}