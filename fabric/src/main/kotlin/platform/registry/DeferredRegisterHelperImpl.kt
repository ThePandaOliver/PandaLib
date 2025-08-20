/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.api.registry.deferred.DeferredObject
import dev.pandasystems.pandalib.core.logger
import dev.pandasystems.pandalib.core.platform.registry.DeferredRegisterHelper
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import java.util.function.Supplier

@AutoService(DeferredRegisterHelper::class)
class DeferredRegisterHelperImpl : DeferredRegisterHelper {
	override fun <T> registerObject(
		deferredObject: DeferredObject<out T>,
		supplier: Supplier<out T>
	) {
		logger.info("Registering ${deferredObject.id} to ${deferredObject.registryKey}")
		@Suppress("UNCHECKED_CAST")
		Registry.register(deferredObject.registry as Registry<T>, deferredObject.id, supplier.get())
	}

	override fun <T> registerNewRegistry(registry: Registry<T>) {
		val registryName = registry.key().location()
		check(!BuiltInRegistries.REGISTRY.containsKey(registryName)) { "Attempted duplicate registration of registry $registryName" }

		@Suppress("UNCHECKED_CAST")
		(BuiltInRegistries.REGISTRY as WritableRegistry<Registry<*>>).register(registry.key() as ResourceKey<Registry<*>>, registry, registry.registryLifecycle())
	}
}