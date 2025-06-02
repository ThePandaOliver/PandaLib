package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLib
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register { server: MinecraftServer? -> Companion.server = server }

		PandaLib // Initialize the core PandaLib functionality
	}

	companion object {
		var server: MinecraftServer? = null
	}
}
