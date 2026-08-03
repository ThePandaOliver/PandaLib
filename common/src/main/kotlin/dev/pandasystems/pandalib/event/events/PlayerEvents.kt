package dev.pandasystems.pandalib.event.events

import dev.pandasystems.pandalib.core.player.PlayerHandle
import dev.pandasystems.pandalib.event.Event
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.event1
import net.minecraft.server.MinecraftServer

val playerServerJoin = event1<PlayerHandle>()
val playerServerLeave = event1<PlayerHandle>()