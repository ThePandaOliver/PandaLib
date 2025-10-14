/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.platform.registry.RendererRegistrationHelper
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

@AutoService(RendererRegistrationHelper::class)
class RendererRegistationHelperImpl : RendererRegistrationHelper {
	override fun <R : Entity> registerEntityRenderer(
		typeProvider: Supplier<EntityType<R>>,
		provider: EntityRendererProvider<R>
	) {
		EntityRenderers.register(typeProvider.get(), provider)
	}

	override fun <R : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
		typeProvider: Supplier<BlockEntityType<R>>,
		provider: BlockEntityRendererProvider<R, S>
	) {
		BlockEntityRenderers.register(typeProvider.get(), provider)
	}
}