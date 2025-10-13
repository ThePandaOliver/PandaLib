/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.platform.registry.RendererRegistrationHelper
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import java.util.function.Supplier

@AutoService(RendererRegistrationHelper::class)
class RendererRegistationHelperImpl : RendererRegistrationHelper {
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