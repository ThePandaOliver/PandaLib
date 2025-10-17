package dev.pandasystems.pandalib

//? if fabric {
import net.fabricmc.api.ModInitializer

//?} else if neoforge {
/*import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.bus.api.IEventBus
import dev.pandasystems.pandalib.registry.addServerReloadListenerEvent
import dev.pandasystems.pandalib.registry.addClientReloadListenerEvent
*///?}

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import org.slf4j.Logger


internal const val MOD_ID = "pandalib"

val logger: Logger = LogUtils.getLogger()

internal fun initializeMod() {
	logger.debug("PandaLib is initializing...")

	pandalibConfig.load()
	ConfigSynchronizer.init()

	logger.debug("PandaLib initialized successfully.")
}

//? if fabric {
class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		initializeMod()
	}
}
//?}

//? if neoforge {
/*@Mod(MOD_ID)
class PandaLibNeoForge(val eventBus: IEventBus) {
	init {
		initializeMod()

		NeoForge.EVENT_BUS.addListener(::addServerReloadListenerEvent)
		eventBus.addListener(::addClientReloadListenerEvent)
	}
}
*///?}