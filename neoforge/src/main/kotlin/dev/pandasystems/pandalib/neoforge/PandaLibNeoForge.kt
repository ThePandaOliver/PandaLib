
package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.neoforge.platform.NetworkHelperImpl
import dev.pandasystems.pandalib.neoforge.platform.RegistrationHelperImpl
import dev.pandasystems.pandalib.platform.Services.REGISTRATION
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddServerReloadListenersEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent

@Mod(PandaLib.MOD_ID)
class PandaLibNeoForge(eventBus: IEventBus) {
	init {
		PandaLib.init()

		eventBus.addListener<RegisterPayloadHandlersEvent> { NetworkHelperImpl.registerPackets(it) }
		if (REGISTRATION is RegistrationHelperImpl) {
			eventBus.addListener<RegisterEvent> { (REGISTRATION as RegistrationHelperImpl).registerEvent(it) }
			eventBus.addListener<NewRegistryEvent> { (REGISTRATION as RegistrationHelperImpl).registerNewRegistryEvent(it) }
			NeoForge.EVENT_BUS.addListener<AddServerReloadListenersEvent> { (REGISTRATION as RegistrationHelperImpl).addServerReloadListenerEvent(it) }
			eventBus.addListener<AddClientReloadListenersEvent> { (REGISTRATION as RegistrationHelperImpl).addClientReloadListenerEvent(it) }
		}
	}
}
