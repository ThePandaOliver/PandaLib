package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.modId
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeConfigurationNetworkManager
import dev.pandasystems.pandalib.neoforge.networking.NeoForgePlayNetworkManager
import dev.pandasystems.pandalib.neoforge.registry.NeoForgePlatformRegistryBridge
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

        eventBus.addListener(NeoForgePlayNetworkManager::registrationEvent)
        eventBus.addListener(NeoForgeConfigurationNetworkManager::registrationEvent)
    }

    companion object {
        lateinit var eventBus: IEventBus
            private set
    }
}
