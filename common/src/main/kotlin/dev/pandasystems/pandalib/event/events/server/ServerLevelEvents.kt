package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelAccessor

data class ServerLevelEventContext(val level: LevelAccessor)

val serverLevelLoad by event<ServerLevelEventContext>()
val serverLevelUnLoad by event<ServerLevelEventContext>()