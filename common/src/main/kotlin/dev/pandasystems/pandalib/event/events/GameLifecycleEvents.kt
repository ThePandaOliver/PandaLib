package dev.pandasystems.pandalib.event.events

import dev.pandasystems.pandalib.event.event1
import net.minecraft.server.MinecraftServer

val serverStarting = event1<MinecraftServer>()
val serverStarted = event1<MinecraftServer>()
val serverStopped = event1<MinecraftServer>()
val serverStopping = event1<MinecraftServer>()
