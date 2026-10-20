package dev.pandasystems.pandalib.core.lifecycles

import dev.pandasystems.pandalib.event.events.server.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

object ServerLifecycle {
	var instance: MinecraftServer? = null
		internal set

	internal fun initialize() = Unit

	init {
		ServerLifecycleEvents.started.subscribe { context ->
			instance = context.server
		}
		ServerLifecycleEvents.stopped.subscribe { context ->
			if (instance === context.server)
				instance = null
		}
	}
}