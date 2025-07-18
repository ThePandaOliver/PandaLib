/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform.registrationhelper

import dev.pandasystems.pandalib.api.registry.deferred.DeferredObject
import dev.pandasystems.pandalib.core.platform.DeferredRegisterHelper
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent
import java.util.function.Consumer
import java.util.function.Supplier

class DeferredRegisterHelperImpl : DeferredRegisterHelper {
	private val pendingRegistries: MutableMap<ResourceKey<out Registry<*>>, PendingRegistries<*>> = mutableMapOf()
	private val pendingRegistryTypes: MutableList<Registry<*>> = mutableListOf()

	override fun <T> registerObject(
		deferredObject: DeferredObject<out T>,
		supplier: Supplier<out T>
	) {
		@Suppress("UNCHECKED_CAST")
		val pending: PendingRegistries<T> = pendingRegistries
			.computeIfAbsent(deferredObject.key.registryKey()) { k: ResourceKey<out Registry<*>> -> PendingRegistries(deferredObject.key.registryKey()) } as PendingRegistries<T>
		pending.add(deferredObject, supplier)
	}

	override fun <T> registerNewRegistry(registry: Registry<T>) {
		pendingRegistryTypes.add(registry)
	}

	fun registerEvent(event: RegisterEvent) {
		pendingRegistries.values.forEach(Consumer { pending: PendingRegistries<*> -> pending.register(event) })
	}

	fun registerNewRegistryEvent(event: NewRegistryEvent) {
		pendingRegistryTypes.forEach(Consumer { registry: Registry<*> -> event.register(registry) })
	}

	private class PendingRegistries<T>(private val registryKey: ResourceKey<out Registry<T>>) {
		private val entries: MutableMap<DeferredObject<out T>, Supplier<out T>> = mutableMapOf<DeferredObject<out T>, Supplier<out T>>()

		fun add(deferredObject: DeferredObject<out T>, objectSupplier: Supplier<out T>) {
			entries.put(deferredObject, objectSupplier)
		}

		fun register(event: RegisterEvent) {
			entries.forEach { (deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) ->
				event.register(registryKey, deferredObject.id) { supplier.get() }
				deferredObject.bind<Any>(false)
			}
		}
	}
}