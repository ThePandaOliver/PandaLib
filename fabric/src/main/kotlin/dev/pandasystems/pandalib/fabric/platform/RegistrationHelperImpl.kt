/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.pandasystems.pandalib.fabric.platform

import dev.pandasystems.pandalib.platform.services.RegistrationHelper
import dev.pandasystems.pandalib.registry.DeferredObject
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Supplier

class RegistrationHelperImpl : RegistrationHelper {
	override fun <T> register(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) {
		@Suppress("UNCHECKED_CAST")
		Registry.register(deferredObject.registry as Registry<T>, deferredObject.id, supplier.get())
	}

	override fun <T> registerNewRegistry(registry: Registry<T>) {
		val registryName = registry.key().location()
		check(BuiltInRegistries.REGISTRY.containsKey(registryName)) { "Attempted duplicate registration of registry $registryName" }

		@Suppress("UNCHECKED_CAST")
		(BuiltInRegistries.REGISTRY as WritableRegistry<Registry<*>>).register(registry.key() as ResourceKey<Registry<*>>, registry, RegistrationInfo.BUILT_IN)
	}

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
