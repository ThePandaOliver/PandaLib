package dev.pandasystems.pandalib.neoforge.client

import dev.pandasystems.pandalib.client.core.PandaLibClientMain
import dev.pandasystems.pandalib.core.modId
import dev.pandasystems.pandalib.neoforge.client.networking.NeoForgeClientConfigurationNetworkManager
import dev.pandasystems.pandalib.neoforge.client.networking.NeoForgeClientPlayNetworkManager
import dev.pandasystems.pandalib.neoforge.client.rendering.NeoForgeRendererRegistry
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
		eventBus.addListener(NeoForgeClientConfigurationNetworkManager.Companion::registrationEvent)
		eventBus.addListener(NeoForgeRendererRegistry::onRegisterRenderers)
	}
}
