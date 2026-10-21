package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLibClientMain
import net.fabricmc.api.ClientModInitializer

internal class FabricClientInit : ClientModInitializer {
	override fun onInitializeClient() {
		PandaLibClientMain()
	}
}
