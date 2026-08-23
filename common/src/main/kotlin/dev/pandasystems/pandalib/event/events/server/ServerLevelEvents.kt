package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel

data class ServerLevelEventContext(val server: MinecraftServer, val level: ServerLevel)

val serverLevelLoad by event<ServerLevelEventContext>()
val serverLevelUnLoad by event<ServerLevelEventContext>()