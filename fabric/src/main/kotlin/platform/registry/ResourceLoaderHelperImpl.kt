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
		ResourceManagerHelper.get(packType).registerReloadListener(object : IdentifiableResourceReloadListener {
			override fun getFabricId(): ResourceLocation {
				return id
			}

			override fun reload(
				preparationBarrier: PreparableReloadListener.PreparationBarrier,
				resourceManager: ResourceManager,
				backgroundExecutor: Executor,
				gameExecutor: Executor
			): CompletableFuture<Void> {
				return listener.reload(preparationBarrier, resourceManager, backgroundExecutor, gameExecutor)
			}

			override fun getFabricDependencies(): Collection<ResourceLocation> {
				return dependencies
			}

			override fun getName(): String {
				return listener.name
			}
		})
	}
}