/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.ResourceLoaderRegistryPlatform
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

@AutoService(ResourceLoaderRegistryPlatform::class)
class ResourceLoaderRegistryImpl : ResourceLoaderRegistryPlatform {
	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: Collection<ResourceLocation>
	) {
		ResourceLoader.get(packType).registerReloader(id, listener)
		dependencies.forEach {
			ResourceLoader.get(packType).addReloaderOrdering(it, id)
		}
	}
}