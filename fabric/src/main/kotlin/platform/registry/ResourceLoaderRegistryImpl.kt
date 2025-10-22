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

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.ResourceLoaderRegistryPlatform
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@AutoService(ResourceLoaderRegistryPlatform::class)
class ResourceLoaderRegistryImpl : ResourceLoaderRegistryPlatform {
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