package dev.pandasystems.pandalib.fabric.testmod

import dev.pandasystems.pandalib.testmod.CommonTestMod
import net.fabricmc.api.ModInitializer

class FabricTestMod : ModInitializer {
    override fun onInitialize() {
        CommonTestMod.init()
    }
}