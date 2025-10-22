/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

@OptIn(InternalPandaLibApi::class)
fun registerResourceLoader(
	packType: PackType,
	listener: PreparableReloadListener,
	id: ResourceLocation,
	dependencies: MutableList<ResourceLocation> = mutableListOf()
) {
	resourceLoaderRegistry.registerReloadListener(packType, listener, id, dependencies)
}

@InternalPandaLibApi
val resourceLoaderRegistry = loadFirstService<ResourceLoaderRegistryPlatform>()

interface ResourceLoaderRegistryPlatform {
	fun registerReloadListener(packType: PackType, listener: PreparableReloadListener, id: ResourceLocation, dependencies: Collection<ResourceLocation>)
}
