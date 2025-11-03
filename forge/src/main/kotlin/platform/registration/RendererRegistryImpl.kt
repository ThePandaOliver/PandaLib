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

package dev.pandasystems.pandalib.forge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.RendererRegistryPlatform
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.client.event.EntityRenderersEvent
import java.util.function.Supplier

@AutoService(RendererRegistryPlatform::class)
class RendererRegistryImpl : RendererRegistryPlatform {
	val entityRendererProviders = mutableMapOf<Supplier<EntityType<*>>, EntityRendererProvider<*>>()
	val blockEntityRendererProviders = mutableMapOf<Supplier<BlockEntityType<*>>, BlockEntityRendererProvider<*>>()

	override fun <R : Entity> registerEntityRenderer(
		typeProvider: Supplier<EntityType<R>>,
		provider: EntityRendererProvider<R>
	) {
		@Suppress("UNCHECKED_CAST")
		entityRendererProviders[typeProvider as Supplier<EntityType<*>>] = provider
	}

	override fun <R : BlockEntity> registerBlockEntityRenderer(
		typeProvider: Supplier<BlockEntityType<R>>,
		provider: BlockEntityRendererProvider<R>
	) {
		@Suppress("UNCHECKED_CAST")
		blockEntityRendererProviders[typeProvider as Supplier<BlockEntityType<*>>] = provider
	}

	@Suppress("UNCHECKED_CAST")
	fun onEntityRendererRegistryEvent(event: EntityRenderersEvent.RegisterRenderers) {
		entityRendererProviders.forEach { event.registerEntityRenderer(it.key.get(), it.value as EntityRendererProvider<in Entity>) }
		blockEntityRendererProviders.forEach { event.registerBlockEntityRenderer(it.key.get(), it.value as BlockEntityRendererProvider<in BlockEntity>) }
	}
}