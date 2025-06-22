/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLib
import dev.pandasystems.pandalib.neoforge.platform.NetworkHelperImpl
import dev.pandasystems.pandalib.neoforge.platform.RegistrationHelperImpl
import dev.pandasystems.pandalib.impl.platform.Services.REGISTRATION
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
		PandaLib // Initialize the core PandaLib functionality

		eventBus.addListener<RegisterPayloadHandlersEvent> { NetworkHelperImpl.registerPackets(it) }
		if (REGISTRATION is RegistrationHelperImpl) {
			eventBus.addListener<RegisterEvent> { (REGISTRATION as RegistrationHelperImpl).registerEvent(it) }
			eventBus.addListener<NewRegistryEvent> { (REGISTRATION as RegistrationHelperImpl).registerNewRegistryEvent(it) }
			NeoForge.EVENT_BUS.addListener<AddServerReloadListenersEvent> { (REGISTRATION as RegistrationHelperImpl).addServerReloadListenerEvent(it) }
			eventBus.addListener<AddClientReloadListenersEvent> { (REGISTRATION as RegistrationHelperImpl).addClientReloadListenerEvent(it) }
		}
	}
}
