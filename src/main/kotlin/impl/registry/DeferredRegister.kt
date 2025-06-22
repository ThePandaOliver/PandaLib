/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.impl.registry

import dev.pandasystems.pandalib.api.platform.registryHelper
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Function
import java.util.function.Supplier

@Suppress("unused")
class DeferredRegister<T> private constructor(private val namespace: String, private val registryKey: ResourceKey<out Registry<T>>) {
	private val entries: MutableMap<DeferredObject<out T>, Supplier<out T>> = HashMap<DeferredObject<out T>, Supplier<out T>>()

	fun <R : T> register(name: String, registryFunc: Function<ResourceKey<T>, R>): DeferredObject<R> {
		return register<R>(ResourceLocation.fromNamespaceAndPath(namespace, name), registryFunc)
	}

	fun <R : T> register(name: String, registrySup: Supplier<R>): DeferredObject<R> {
		return register<R>(ResourceLocation.fromNamespaceAndPath(namespace, name), registrySup)
	}

	fun <R : T> register(name: ResourceLocation, registryFunc: Function<ResourceKey<T>, R>): DeferredObject<R> {
		val key = ResourceKey.create<T>(registryKey, name)
		return register<R>(key, Supplier { registryFunc.apply(key) })
	}

	fun <R : T> register(name: ResourceLocation, registrySup: Supplier<R>): DeferredObject<R> {
		return register<R>(ResourceKey.create<T>(registryKey, name), registrySup)
	}

	private fun <R : T> register(resourceKey: ResourceKey<T>, registrySup: Supplier<R>): DeferredObject<R> {
		val deferredObject = DeferredObject<R>(resourceKey)
		entries.put(deferredObject, registrySup)
		return deferredObject
	}

	fun register() {
		entries.forEach { (deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) -> registryHelper.register(deferredObject, supplier) }
	}

	companion object {
		@JvmStatic
		fun <T> create(namespace: String, registry: Registry<T>): DeferredRegister<T> {
			return create<T>(namespace, registry.key())
		}

		@JvmStatic
		fun <T> create(namespace: String, registryLocation: ResourceLocation): DeferredRegister<T> {
			return create<T>(namespace, ResourceKey.createRegistryKey<T>(registryLocation))
		}

		@JvmStatic
		fun <T> create(namespace: String, registryKey: ResourceKey<out Registry<T>>): DeferredRegister<T> {
			return DeferredRegister<T>(namespace, registryKey)
		}
	}
}
