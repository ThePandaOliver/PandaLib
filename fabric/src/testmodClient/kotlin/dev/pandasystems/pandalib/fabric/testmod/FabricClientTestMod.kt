package dev.pandasystems.pandalib.fabric.testmod

import dev.pandasystems.pandalib.testmod.CommonClientTestMod
import net.fabricmc.api.ClientModInitializer

class FabricClientTestMod : ClientModInitializer {
	override fun onInitializeClient() {
		CommonClientTestMod.initClient()
	}
}
