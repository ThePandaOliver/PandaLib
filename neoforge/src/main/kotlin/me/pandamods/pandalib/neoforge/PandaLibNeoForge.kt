/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.neoforge

import me.pandamods.pandalib.PandaLib
import me.pandamods.pandalib.neoforge.platform.NetworkHelperImpl
import me.pandamods.pandalib.neoforge.platform.RegistrationHelperImpl
import me.pandamods.pandalib.platform.Services.REGISTRATION
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddReloadListenerEvent
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
			NeoForge.EVENT_BUS.addListener<AddReloadListenerEvent> { (REGISTRATION as RegistrationHelperImpl).addReloadListenerEvent(it) }
		}
	}
}
