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
package me.pandamods.pandalib.neoforge.platform

import me.pandamods.pandalib.platform.services.RegistrationHelper
import me.pandamods.pandalib.registry.DeferredObject
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ReloadableResourceManager
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrationHelperImpl : RegistrationHelper {
	private val pendingRegistries: MutableMap<ResourceKey<out Registry<*>>, PendingRegistries<*>> =
		HashMap<ResourceKey<out Registry<*>>, PendingRegistries<*>>()
	private val pendingRegistryTypes: MutableList<Registry<*>> = ArrayList<Registry<*>>()
	private val serverDataReloadListeners: MutableList<PreparableReloadListener> = ArrayList<PreparableReloadListener>()

	override fun <T> register(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) {
//		val pending: PendingRegistries<T> = pendingRegistries
//			.computeIfAbsent(deferredObject.key.registryKey()) { k: ResourceKey<out Registry<*>> -> PendingRegistries(deferredObject.key.registryKey()) } as PendingRegistries<T>
//		pending.add(deferredObject, supplier)
	}

	override fun <T> registerNewRegistry(registry: Registry<T>) {
		pendingRegistryTypes.add(registry)
	}

	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: MutableList<ResourceLocation>
	) {
		if (packType == PackType.SERVER_DATA) {
			serverDataReloadListeners.add(listener)
		} else {
			(Minecraft.getInstance().getResourceManager() as ReloadableResourceManager).registerReloadListener(listener)
		}
	}

	fun registerEvent(event: RegisterEvent) {
		pendingRegistries.values.forEach(Consumer { pending: PendingRegistries<*> -> pending!!.register(event) })
	}

	fun registerNewRegistryEvent(event: NewRegistryEvent) {
		pendingRegistryTypes.forEach(Consumer { registry: Registry<*> -> event.register(registry) })
	}

	fun addReloadListenerEvent(event: AddReloadListenerEvent) {
		serverDataReloadListeners.forEach(Consumer { listener: PreparableReloadListener -> event.addListener(listener) })
	}

	private class PendingRegistries<T>(private val registryKey: ResourceKey<out Registry<T>>) {
		private val entries: MutableMap<DeferredObject<out T>, Supplier<out T>> = HashMap<DeferredObject<out T>, Supplier<out T>>()

		fun add(deferredObject: DeferredObject<out T>, objectSupplier: Supplier<out T>) {
			entries.put(deferredObject, objectSupplier)
		}

		fun register(event: RegisterEvent) {
			entries.forEach { (deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) ->
				event.register<T>(registryKey, deferredObject!!.id) { supplier!!.get() }
				deferredObject.bind<Any>(false)
			}
		}
	}
}
