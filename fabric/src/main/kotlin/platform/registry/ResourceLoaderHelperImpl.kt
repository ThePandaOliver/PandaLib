/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.platform.registry.ResourceLoaderHelper
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@AutoService(ResourceLoaderHelper::class)
class ResourceLoaderHelperImpl : ResourceLoaderHelper {
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