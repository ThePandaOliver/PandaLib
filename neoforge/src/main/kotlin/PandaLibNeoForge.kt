/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.initializePandaLib
import dev.pandasystems.pandalib.neoforge.platform.registration.DeferredRegisterImpl
import dev.pandasystems.pandalib.neoforge.platform.registration.RendererRegistryImpl
import dev.pandasystems.pandalib.neoforge.platform.registration.ResourceLoaderRegistryImpl
import dev.pandasystems.pandalib.pandalibModid
import dev.pandasystems.pandalib.registry.deferred.deferredRegister
import dev.pandasystems.pandalib.registry.rendererRegistry
import dev.pandasystems.pandalib.registry.resourceLoaderRegistry
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge

@OptIn(InternalPandaLibApi::class)
@Mod(pandalibModid)
class PandaLibNeoForge(val eventBus: IEventBus) {
	init {
		eventBus.addListener((deferredRegister as DeferredRegisterImpl)::registerEvent)
		eventBus.addListener((deferredRegister as DeferredRegisterImpl)::registerNewRegistryEvent)

		NeoForge.EVENT_BUS.addListener((resourceLoaderRegistry as ResourceLoaderRegistryImpl)::addServerReloadListenerEvent)

		eventBus.addListener((rendererRegistry as RendererRegistryImpl)::onEntityRendererRegistryEvent)

		initializePandaLib()
	}
}
