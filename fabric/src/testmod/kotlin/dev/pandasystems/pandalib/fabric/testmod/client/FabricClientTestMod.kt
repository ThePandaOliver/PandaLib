package dev.pandasystems.pandalib.fabric.testmod.client

import dev.pandasystems.pandalib.testmod.CommonClientTestMod
import net.fabricmc.api.ClientModInitializer

class FabricClientTestMod : ClientModInitializer {
	override fun onInitializeClient() {
		CommonClientTestMod.initClient()
	}
}
