package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLibMain
import net.fabricmc.api.ModInitializer

private class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandaLibMain(
            FabricNetworkManager()
        )
    }
}