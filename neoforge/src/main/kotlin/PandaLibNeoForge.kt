/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.PandaLib
import dev.pandasystems.pandalib.core.platform.registry.deferredRegisterHelper
import dev.pandasystems.pandalib.core.platform.registry.rendererRegistrationHelper
import dev.pandasystems.pandalib.core.platform.registry.resourceLoaderHelper
import dev.pandasystems.pandalib.neoforge.platform.registration.DeferredRegisterHelperImpl
import dev.pandasystems.pandalib.neoforge.platform.registration.RendererRegistationHelperImpl
import dev.pandasystems.pandalib.neoforge.platform.registration.ResourceLoaderHelperImpl
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge

@Mod(PandaLib.MOD_ID)
class PandaLibNeoForge(val eventBus: IEventBus) {
	init {
		PandaLib // Initialize the core PandaLib functionality

		if (deferredRegisterHelper is DeferredRegisterHelperImpl) {
			val impl = deferredRegisterHelper as DeferredRegisterHelperImpl
			eventBus.addListener(impl::registerEvent)
			eventBus.addListener(impl::registerNewRegistryEvent)
		}

		if (resourceLoaderHelper is ResourceLoaderHelperImpl) {
			val impl = resourceLoaderHelper as ResourceLoaderHelperImpl
			NeoForge.EVENT_BUS.addListener(impl::addServerReloadListenerEvent)
			eventBus.addListener(impl::addClientReloadListenerEvent)
		}

		if (rendererRegistrationHelper is RendererRegistationHelperImpl) {
			val impl = rendererRegistrationHelper as RendererRegistationHelperImpl
			eventBus.addListener(impl::onEntityRendererRegistryEvent)
		}
	}
}
