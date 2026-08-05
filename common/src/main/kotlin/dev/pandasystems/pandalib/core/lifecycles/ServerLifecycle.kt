package dev.pandasystems.pandalib.core.lifecycles

import dev.pandasystems.pandalib.event.events.serverStarted
import dev.pandasystems.pandalib.event.events.serverStopped
import net.minecraft.server.MinecraftServer

object ServerLifecycle {
	var serverInstance: MinecraftServer? = null
		internal set

	fun initialize() = Unit

	init {
		serverStarted.subscribe { startedServer ->
			serverInstance = startedServer
		}
		serverStopped.subscribe { stoppedServer ->
			if (serverInstance === stoppedServer)
				serverInstance = null
		}
	}
}