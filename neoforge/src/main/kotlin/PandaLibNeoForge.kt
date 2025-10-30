/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.neoforge.platform.registration.DeferredRegisterImpl
import dev.pandasystems.pandalib.neoforge.platform.registration.RendererRegistryImpl
import dev.pandasystems.pandalib.neoforge.platform.registration.ResourceLoaderRegistryImpl
import dev.pandasystems.pandalib.registry.deferred.deferredRegister
import dev.pandasystems.pandalib.registry.rendererRegistry
import dev.pandasystems.pandalib.registry.resourceLoaderRegistry
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge

@OptIn(InternalPandaLibApi::class)
@Mod(PandaLib.modid)
class PandaLibNeoForge(val eventBus: IEventBus) {
	init {
		eventBus.addListener((deferredRegister as DeferredRegisterImpl)::registerEvent)
		eventBus.addListener((deferredRegister as DeferredRegisterImpl)::registerNewRegistryEvent)

		NeoForge.EVENT_BUS.addListener((resourceLoaderRegistry as ResourceLoaderRegistryImpl)::addServerReloadListenerEvent)

		eventBus.addListener((rendererRegistry as RendererRegistryImpl)::onEntityRendererRegistryEvent)

		PandaLib

		if (gameEnvironment.isClient) {
			dev.pandasystems.pandalib.neoforge.client.PandaLibClientNeoForge(eventBus)
		}
	}
}
