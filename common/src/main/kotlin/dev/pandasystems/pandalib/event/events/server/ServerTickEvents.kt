package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event
import net.minecraft.server.MinecraftServer

data class ServerTickEventContext(val server: MinecraftServer)

val startServerTick by event<ServerTickEventContext>()
val endServerTick by event<ServerTickEventContext>()