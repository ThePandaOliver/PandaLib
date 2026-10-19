package dev.pandasystems.pandalib.neoforge.event.events.server

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.event.Event
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationConnectionEventContext
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationEvents
import dev.pandasystems.pandalib.neoforge.NeoForgeInit
import dev.pandasystems.pandalib.neoforge.event.bindEvent
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent

@AutoService(ServerConfigurationEvents::class)
class ServerConfigurationEventsImpl : ServerConfigurationEvents {
    override val configurationStart: Event<ServerConfigurationConnectionEventContext> = NeoForgeInit.eventBus.bindEvent(
        convertFromCtx = { ctx -> RegisterConfigurationTasksEvent(ctx.listener) },
        convertToCtx = { event -> ServerConfigurationConnectionEventContext(event.listener) }
    )
}