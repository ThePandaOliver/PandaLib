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

package dev.pandasystems.pandalib.registry.deferred

import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import java.util.function.Supplier

class DeferredRegister<T : Any> private constructor(private val namespace: String, private val registryKey: ResourceKey<out Registry<T>>) {
	private val entries = mutableMapOf<DeferredObject<out T>, Supplier<out T>>()

	fun <R : T> register(name: String, registryEntry: (ResourceKey<T>) -> R): DeferredObject<R> {
		return register(Identifier.fromNamespaceAndPath(namespace, name), registryEntry)
	}

	fun <R : T> register(name: Identifier, registryEntry: (ResourceKey<T>) -> R): DeferredObject<R> {
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
		fun <T : Any> create(namespace: String, registry: Registry<T>): DeferredRegister<T> {
			return create<T>(namespace, registry.key())
		}

		fun <T : Any> create(namespace: String, registryLocation: Identifier): DeferredRegister<T> {
			return create<T>(namespace, ResourceKey.createRegistryKey<T>(registryLocation))
		}

		fun <T : Any> create(namespace: String, registryKey: ResourceKey<out Registry<T>>): DeferredRegister<T> {
			return DeferredRegister(namespace, registryKey)
		}

		@OptIn(InternalPandaLibApi::class)
		fun <T : Any, R : Registry<T>> registerNewRegistry(registry: R): R {
			deferredRegister.registerNewRegistry(registry)
			return registry
		}
	}
}

@InternalPandaLibApi
val deferredRegister = loadFirstService<DeferredRegisterPlatform>()

interface DeferredRegisterPlatform {
	fun <T : Any> registerObject(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>)
	fun <T : Any> registerNewRegistry(registry: Registry<T>)
}
