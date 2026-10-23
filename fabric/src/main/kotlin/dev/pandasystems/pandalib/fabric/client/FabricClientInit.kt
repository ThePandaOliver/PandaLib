package dev.pandasystems.pandalib.fabric.client

import dev.pandasystems.pandalib.client.core.PandaLibClientMain
import net.fabricmc.api.ClientModInitializer

internal class FabricClientInit : ClientModInitializer {
	override fun onInitializeClient() {
		PandaLibClientMain()
	}
}
