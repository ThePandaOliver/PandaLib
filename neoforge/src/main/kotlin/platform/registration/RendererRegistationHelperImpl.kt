/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.platform.RendererRegistrationHelper
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@AutoService(RendererRegistrationHelper::class)
class RendererRegistationHelperImpl : RendererRegistrationHelper {
	val entityRendererProviders = mutableMapOf<EntityType<*>, EntityRendererProvider<*>>()
	val blockEntityRendererProviders = mutableMapOf<BlockEntityType<*>, BlockEntityRendererProvider<*>>()

	override fun <R : Entity> registerEntityRenderer(
		type: EntityType<R>,
		provider: EntityRendererProvider<R>
	) {
		entityRendererProviders[type] = provider
	}

	override fun <R : BlockEntity> registerBlockEntityRenderer(
		type: BlockEntityType<R>,
		provider: BlockEntityRendererProvider<R>
	) {
		blockEntityRendererProviders[type] = provider
	}

	@Suppress("UNCHECKED_CAST")
	fun onEntityRendererRegistryEvent(event: EntityRenderersEvent.RegisterRenderers) {
		entityRendererProviders.forEach { event.registerEntityRenderer(it.key, it.value as EntityRendererProvider<in Entity>) }
		blockEntityRendererProviders.forEach { event.registerBlockEntityRenderer(it.key, it.value as BlockEntityRendererProvider<in BlockEntity>) }
	}
}