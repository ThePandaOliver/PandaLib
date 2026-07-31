package dev.pandasystems.pandalib.networking.minecraft.fabric

import dev.pandasystems.pandalib.networking.minecraft.PandaLibNetworkMain
import net.fabricmc.api.ModInitializer

private class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandaLibNetworkMain(
            FabricNetworkManager()
        )
    }
}