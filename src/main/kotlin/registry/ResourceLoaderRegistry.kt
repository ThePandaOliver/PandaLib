/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

@OptIn(InternalPandaLibApi::class)
fun registerResourceLoader(
	packType: PackType,
	listener: PreparableReloadListener,
	id: Identifier,
	dependencies: MutableList<Identifier> = mutableListOf()
) {
	resourceLoaderRegistry.registerReloadListener(packType, listener, id, dependencies)
}

@InternalPandaLibApi
val resourceLoaderRegistry = loadFirstService<ResourceLoaderRegistryPlatform>()

interface ResourceLoaderRegistryPlatform {
	fun registerReloadListener(packType: PackType, listener: PreparableReloadListener, id: Identifier, dependencies: Collection<Identifier>)
}
