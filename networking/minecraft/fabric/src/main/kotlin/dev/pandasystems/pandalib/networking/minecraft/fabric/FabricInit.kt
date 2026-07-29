package dev.pandasystems.pandalib.networking.minecraft.fabric

import dev.pandasystems.pandalib.networking.minecraft.PandalibNetwork
import net.fabricmc.api.ModInitializer

private class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandalibNetwork(
            registrar = FabricNetworkRegistrar()
        )
    }
}