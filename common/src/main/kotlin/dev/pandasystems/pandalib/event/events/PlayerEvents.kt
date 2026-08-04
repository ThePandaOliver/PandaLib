package dev.pandasystems.pandalib.event.events

import dev.pandasystems.pandalib.core.player.PlayerHandle
import dev.pandasystems.pandalib.core.player.ServerPlayerHandle
import dev.pandasystems.pandalib.event.Event
import dev.pandasystems.pandalib.event.event
import dev.pandasystems.pandalib.event.event1
import dev.pandasystems.pandalib.event.event3
import net.minecraft.server.MinecraftServer

val playerServerJoin = event1<ServerPlayerHandle>()
val playerServerLeave = event1<ServerPlayerHandle>()
val playerServerAfterRespawn = event3<ServerPlayerHandle, ServerPlayerHandle, Boolean>()
val playerServerCopyFrom = event3<ServerPlayerHandle, ServerPlayerHandle, Boolean>()