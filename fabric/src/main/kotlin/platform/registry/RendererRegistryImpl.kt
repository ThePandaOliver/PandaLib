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

package dev.pandasystems.pandalib.fabric.platform.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.RendererRegistryPlatform
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

@AutoService(RendererRegistryPlatform::class)
class RendererRegistryImpl : RendererRegistryPlatform {
	override fun <R : Entity> registerEntityRenderer(
		typeProvider: Supplier<EntityType<R>>,
		provider: EntityRendererProvider<R>
	) {
		EntityRendererRegistry.register(typeProvider.get(), provider)
	}

	override fun <R : BlockEntity> registerBlockEntityRenderer(
		typeProvider: Supplier<BlockEntityType<R>>,
		provider: BlockEntityRendererProvider<R>
	) {
		BlockEntityRenderers.register(typeProvider.get(), provider)
	}
}