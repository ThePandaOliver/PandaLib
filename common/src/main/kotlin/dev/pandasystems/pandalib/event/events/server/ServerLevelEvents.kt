package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event2
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel

val serverLevelLoad = event2<MinecraftServer, ServerLevel>()
val serverLevelUnLoad = event2<MinecraftServer, ServerLevel>()