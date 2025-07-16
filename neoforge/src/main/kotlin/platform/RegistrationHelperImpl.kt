/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform

import dev.pandasystems.pandalib.core.platform.RegistrationHelper
import dev.pandasystems.pandalib.api.registry.deferred.DeferredObject
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.event.AddServerReloadListenersEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent
import java.util.function.Consumer
import java.util.function.Supplier

class RegistrationHelperImpl : RegistrationHelper {
	private val pendingRegistries: MutableMap<ResourceKey<out Registry<*>>, PendingRegistries<*>> = mutableMapOf()
	private val pendingRegistryTypes: MutableList<Registry<*>> = mutableListOf()
	private val serverDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()
	private val clientDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()

	override fun <T> register(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>) {
		@Suppress("UNCHECKED_CAST")
		val pending: PendingRegistries<T> = pendingRegistries
			.computeIfAbsent(deferredObject.key.registryKey()) { k: ResourceKey<out Registry<*>> -> PendingRegistries(deferredObject.key.registryKey()) } as PendingRegistries<T>
		pending.add(deferredObject, supplier)
	}

	override fun <T> registerNewRegistry(registry: Registry<T>) {
		pendingRegistryTypes.add(registry)
	}

	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: Collection<ResourceLocation>
	) {
		if (packType == PackType.SERVER_DATA) {
			serverDataReloadListeners += id to listener
		} else {
			clientDataReloadListeners += id to listener
		}
	}

	fun registerEvent(event: RegisterEvent) {
		pendingRegistries.values.forEach(Consumer { pending: PendingRegistries<*> -> pending.register(event) })
	}

	fun registerNewRegistryEvent(event: NewRegistryEvent) {
		pendingRegistryTypes.forEach(Consumer { registry: Registry<*> -> event.register(registry) })
	}

	fun addServerReloadListenerEvent(event: AddServerReloadListenersEvent) {
		serverDataReloadListeners.forEach(Consumer { (id, listener) -> event.addListener(id, listener) })
	}

	fun addClientReloadListenerEvent(event: AddClientReloadListenersEvent) {
		clientDataReloadListeners.forEach(Consumer { (id, listener) -> event.addListener(id, listener) })
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

	val entityRendererProviders = mutableMapOf<EntityType<*>, EntityRendererProvider<*>>()
	val blockEntityRendererProviders = mutableMapOf<BlockEntityType<*>, BlockEntityRendererProvider<*>>()

	override fun <R : Entity> registerEntityRenderer(
		type: EntityType<R>,
		provider: EntityRendererProvider<R>
	) {
		entityRendererProviders.put(type, provider)
	}

	override fun <R : BlockEntity> registerBlockEntityRenderer(
		type: BlockEntityType<R>,
		provider: BlockEntityRendererProvider<R>
	) {
		blockEntityRendererProviders.put(type, provider)
	}

	@Suppress("UNCHECKED_CAST")
	fun onEntityRendererRegistryEvent(event: EntityRenderersEvent.RegisterRenderers) {
		entityRendererProviders.forEach { event.registerEntityRenderer(it.key, it.value as EntityRendererProvider<in Entity>) }
		blockEntityRendererProviders.forEach { event.registerBlockEntityRenderer(it.key, it.value as BlockEntityRendererProvider<in BlockEntity>) }
	}
}
