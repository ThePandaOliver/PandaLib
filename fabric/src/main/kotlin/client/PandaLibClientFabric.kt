package dev.pandasystems.pandalib.fabric.client

import dev.pandasystems.pandalib.core.client.PandaLibClient
import net.fabricmc.api.ClientModInitializer

class PandaLibClientFabric : ClientModInitializer {
	override fun onInitializeClient() {
		PandaLibClient
	}
}
