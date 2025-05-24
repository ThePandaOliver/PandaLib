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
package me.pandamods.pandalib.registry

import me.pandamods.pandalib.platform.Services
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
		entries.forEach { (deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) -> Services.REGISTRATION.register(deferredObject, supplier) }
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
