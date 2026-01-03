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
import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.registry.deferred.DeferredObject
import dev.pandasystems.pandalib.registry.deferred.DeferredRegisterPlatform
import net.minecraft.core.RegistrationInfo
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import java.util.function.Supplier

@AutoService(DeferredRegisterPlatform::class)
class DeferredRegisterImpl : DeferredRegisterPlatform {
	override fun <T : Any> registerObject(
		deferredObject: DeferredObject<out T>,
		supplier: Supplier<out T>
	) {
		PandaLib.logger.info("Registering ${deferredObject.id} to ${deferredObject.registryKey}")
		@Suppress("UNCHECKED_CAST")
		Registry.register(deferredObject.registry as Registry<T>, deferredObject.id, supplier.get())
	}

	override fun <T : Any> registerNewRegistry(registry: Registry<T>) {
		val registryName = registry.key().identifier()
		check(!BuiltInRegistries.REGISTRY.containsKey(registryName)) { "Attempted duplicate registration of registry $registryName" }

		@Suppress("UNCHECKED_CAST")
		(BuiltInRegistries.REGISTRY as WritableRegistry<Registry<*>>).register(registry.key() as ResourceKey<Registry<*>>, registry, RegistrationInfo.BUILT_IN)
	}
}