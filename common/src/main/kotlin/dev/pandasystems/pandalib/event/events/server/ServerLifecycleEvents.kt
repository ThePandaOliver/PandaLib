package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event
import net.minecraft.server.MinecraftServer

data class ServerLifecycleEventContext(val server: MinecraftServer)

val serverStarting by event<ServerLifecycleEventContext>()
val serverStarted by event<ServerLifecycleEventContext>()
val serverStopped by event<ServerLifecycleEventContext>()
val serverStopping by event<ServerLifecycleEventContext>()
