package dev.pandasystems.pandalib.core

import dev.pandasystems.pandalib.event.events.serverStarted
import dev.pandasystems.pandalib.event.events.serverStopped
import net.minecraft.server.MinecraftServer
import net.minecraft.client.Minecraft as MinecraftClient

object GameLifecycle {
	val clientInstance: MinecraftClient get() = MinecraftClient.getInstance()
	var serverInstance: MinecraftServer? = null
		internal set

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