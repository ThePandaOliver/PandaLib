/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry.deferred

import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class DeferredRegister<T> private constructor(private val namespace: String, private val registryKey: ResourceKey<out Registry<T>>) {
	private val entries = mutableMapOf<DeferredObject<out T>, Supplier<out T>>()

	fun <R : T> register(name: String, registryEntry: (ResourceKey<T>) -> R): DeferredObject<R> {
		return register(ResourceLocation(namespace, name), registryEntry)
	}

	fun <R : T> register(name: ResourceLocation, registryEntry: (ResourceKey<T>) -> R): DeferredObject<R> {
		val key = ResourceKey.create(registryKey, name)
		return register(key, registryEntry)
	}

	private fun <R : T> register(resourceKey: ResourceKey<T>, registryEntry: (ResourceKey<T>) -> R): DeferredObject<R> {
		val deferredObject = DeferredObject<R>(resourceKey)
		entries[deferredObject] = Supplier { registryEntry(resourceKey) }
		return deferredObject
	}

	@OptIn(InternalPandaLibApi::class)
	fun register() {
		entries.forEach { (deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) -> deferredRegister.registerObject(deferredObject, supplier) }
	}

	companion object {
		fun <T> create(namespace: String, registry: Registry<T>): DeferredRegister<T> {
			return create<T>(namespace, registry.key())
		}

		fun <T> create(namespace: String, registryLocation: ResourceLocation): DeferredRegister<T> {
			return create<T>(namespace, ResourceKey.createRegistryKey<T>(registryLocation))
		}

		fun <T> create(namespace: String, registryKey: ResourceKey<out Registry<T>>): DeferredRegister<T> {
			return DeferredRegister(namespace, registryKey)
		}

		@OptIn(InternalPandaLibApi::class)
		fun <T, R : Registry<T>> registerNewRegistry(registry: R): R {
			deferredRegister.registerNewRegistry(registry)
			return registry
		}
	}
}

@InternalPandaLibApi
val deferredRegister = loadFirstService<DeferredRegisterPlatform>()

interface DeferredRegisterPlatform {
	fun <T> registerObject(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>)
	fun <T> registerNewRegistry(registry: Registry<T>)
}
