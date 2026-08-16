package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.PandaLibMain
import dev.pandasystems.pandalib.core.handles.player.handle
import dev.pandasystems.pandalib.event.events.*
import dev.pandasystems.pandalib.event.events.server.serverStarted
import dev.pandasystems.pandalib.event.events.server.serverStarting
import dev.pandasystems.pandalib.event.events.server.serverStopped
import dev.pandasystems.pandalib.event.events.server.serverStopping
import dev.pandasystems.pandalib.fabric.networking.FabricNetworkManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents

internal class FabricInit : ModInitializer {
    override fun onInitialize() {
        PandaLibMain(
	        FabricNetworkManager(),
            FabricRuntime
        )
    }
}