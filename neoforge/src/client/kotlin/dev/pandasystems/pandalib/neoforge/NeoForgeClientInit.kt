package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLibClientMain
import dev.pandasystems.pandalib.core.modId
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeClientConfigurationNetworkManager
import dev.pandasystems.pandalib.neoforge.networking.NeoForgeClientPlayNetworkManager
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(value = modId, dist = [Dist.CLIENT])
internal class NeoForgeClientInit(
	eventBus: IEventBus
) {
	init {
		PandaLibClientMain()

		eventBus.addListener(NeoForgeClientPlayNetworkManager::registrationEvent)
		eventBus.addListener(NeoForgeClientConfigurationNetworkManager::registrationEvent)
	}
}
