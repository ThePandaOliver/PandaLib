package dev.pandasystems.pandalib.event.events.server

import dev.pandasystems.pandalib.event.event1
import net.minecraft.server.MinecraftServer

val startServerTick = event1<MinecraftServer>()
val endServerTick = event1<MinecraftServer>()