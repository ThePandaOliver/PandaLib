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

package dev.pandasystems.pandalib.forge

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.forge.client.PandaLibClientForge
import dev.pandasystems.pandalib.forge.platform.registration.DeferredRegisterImpl
import dev.pandasystems.pandalib.forge.platform.registration.RendererRegistryImpl
import dev.pandasystems.pandalib.forge.platform.registration.ResourceLoaderRegistryImpl
import dev.pandasystems.pandalib.registry.deferred.deferredRegister
import dev.pandasystems.pandalib.registry.rendererRegistry
import dev.pandasystems.pandalib.registry.resourceLoaderRegistry
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext



@OptIn(InternalPandaLibApi::class)
@Mod(PandaLib.modid)
class PandaLibForge(context: FMLJavaModLoadingContext) {
	init {
		val eventBus = context.modEventBus
		eventBus.addListener((deferredRegister as DeferredRegisterImpl)::registerEvent)

		MinecraftForge.EVENT_BUS.addListener((resourceLoaderRegistry as ResourceLoaderRegistryImpl)::addServerReloadListenerEvent)

		eventBus.addListener((rendererRegistry as RendererRegistryImpl)::onEntityRendererRegistryEvent)

		PandaLib

		if (gameEnvironment.isClient) {
			PandaLibClientForge(eventBus)
		}
	}
}
