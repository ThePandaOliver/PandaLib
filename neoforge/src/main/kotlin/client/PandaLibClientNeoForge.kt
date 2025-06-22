package dev.pandasystems.pandalib.neoforge.client

import dev.pandasystems.pandalib.core.PandaLib
import dev.pandasystems.pandalib.core.client.PandaLibClient
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(value = PandaLib.MOD_ID, dist = [Dist.CLIENT])
class PandaLibClientNeoForge(eventBus: IEventBus) {
	init {
		PandaLibClient
	}
}
