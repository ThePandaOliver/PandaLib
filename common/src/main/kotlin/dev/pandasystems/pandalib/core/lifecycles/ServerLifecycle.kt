package dev.pandasystems.pandalib.core.lifecycles

import dev.pandasystems.pandalib.event.events.server.serverStarted
import dev.pandasystems.pandalib.event.events.server.serverStopped
import net.minecraft.server.MinecraftServer

object ServerLifecycle {
	var serverInstance: MinecraftServer? = null
		internal set

	internal fun initialize() = Unit

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