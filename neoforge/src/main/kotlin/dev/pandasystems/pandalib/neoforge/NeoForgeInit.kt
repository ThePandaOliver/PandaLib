package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.modId
import dev.pandasystems.pandalib.core.utils.loadService
import dev.pandasystems.pandalib.event.events.server.ServerConfigurationEvents
import dev.pandasystems.pandalib.neoforge.event.events.server.ServerConfigurationEventsImpl
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeConfigurationNetworkManager
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeNetworkManager
import dev.pandasystems.pandalib.neoforge.registry.NeoForgePlatformRegistryBridge
import dev.pandasystems.pandalib.networking.ConfigurationNetworkManager
import dev.pandasystems.pandalib.networking.NetworkManager
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(modId)
internal class NeoForgeInit(
    eventBus: IEventBus
) {
    init {
        Companion.eventBus = eventBus
        PandaLibMain()

        eventBus.addListener(NeoForgePlatformRegistryBridge::onRegisterEvent)

        eventBus.addListener(NeoForgeNetworkManager::registrationEvent)
        eventBus.addListener(NeoForgeConfigurationNetworkManager::registrationEvent)
    }

    companion object {
        lateinit var eventBus: IEventBus
            private set
    }
}
