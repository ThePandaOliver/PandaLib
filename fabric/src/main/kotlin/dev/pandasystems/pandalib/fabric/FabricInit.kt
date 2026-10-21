package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLibMain
import net.fabricmc.api.ModInitializer

internal class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandaLibMain()
    }
}