package dev.pandasystems.pandalib.core.lifecycles

import dev.pandasystems.pandalib.event.events.server.serverStarted
import dev.pandasystems.pandalib.event.events.server.serverStopped
import net.minecraft.server.MinecraftServer

object ServerLifecycle {
	var serverInstance: MinecraftServer? = null
		internal set

	internal fun initialize() = Unit

	init {
		serverStarted.subscribe { context ->
			serverInstance = context.server
		}
		serverStopped.subscribe { context ->
			if (serverInstance === context.server)
				serverInstance = null
		}
	}
}