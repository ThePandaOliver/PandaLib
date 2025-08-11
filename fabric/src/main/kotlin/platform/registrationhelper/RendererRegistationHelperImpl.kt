/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform.registrationhelper

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.platform.RendererRegistrationHelper
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@AutoService(RendererRegistrationHelper::class)
class RendererRegistationHelperImpl : RendererRegistrationHelper {
	override fun <R : Entity> registerEntityRenderer(
		type: EntityType<R>,
		provider: EntityRendererProvider<R>
	) {
		EntityRendererRegistry.register<R>(type, provider)
	}

	override fun <R : BlockEntity> registerBlockEntityRenderer(
		type: BlockEntityType<R>,
		provider: BlockEntityRendererProvider<R>
	) {
		BlockEntityRenderers.register(type, provider)
	}
}