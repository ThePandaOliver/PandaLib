package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event
import net.minecraft.server.MinecraftServer

data class ServerTickEventContext(val server: MinecraftServer)

val preServerTick by event<ServerTickEventContext>()
val postServerTick by event<ServerTickEventContext>()