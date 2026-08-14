package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.modId
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeNetworkManager
import net.neoforged.bus.EventBus
import net.neoforged.fml.common.Mod

@Mod(modId)
private class NeoForgeInit(
    eventBus: EventBus
) {
    init {
        PandaLibMain(
            NeoForgeNetworkManager,
            NeoForgeRuntime
        )


    }
}