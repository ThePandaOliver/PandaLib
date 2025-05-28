package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLib
import dev.pandasystems.pandalib.event.events.NetworkingEvents
import dev.pandasystems.pandalib.impl.platform.Services
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register { server: MinecraftServer? -> Companion.server = server }

		PandaLib.init()
		NetworkingEvents.PACKET_PAYLOAD_REGISTRY.invoker().register(Services.NETWORK)
	}

	companion object {
		var server: MinecraftServer? = null
	}
}
