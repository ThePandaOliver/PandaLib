/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.ResourceLoaderRegistryPlatform
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

@AutoService(ResourceLoaderRegistryPlatform::class)
class ResourceLoaderRegistryImpl : ResourceLoaderRegistryPlatform {
	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: Identifier,
		dependencies: Collection<Identifier>
	) {
		ResourceLoader.get(packType).registerReloader(id, listener)
		dependencies.forEach {
			ResourceLoader.get(packType).addReloaderOrdering(it, id)
		}
	}
}