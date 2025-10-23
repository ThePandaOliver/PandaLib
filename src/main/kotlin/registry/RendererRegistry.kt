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

package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

@OptIn(InternalPandaLibApi::class)
fun <R : Entity> registerEntityRenderer(typeProvider: Supplier<EntityType<R>>, provider: EntityRendererProvider<R>) {
	rendererRegistry.registerEntityRenderer(typeProvider, provider)
}

@OptIn(InternalPandaLibApi::class)
fun <R : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(typeProvider: Supplier<BlockEntityType<R>>, provider: BlockEntityRendererProvider<R, S>) {
	rendererRegistry.registerBlockEntityRenderer(typeProvider, provider)
}

@InternalPandaLibApi
val rendererRegistry = loadFirstService<RendererRegistryPlatform>()

interface RendererRegistryPlatform {
	fun <R : Entity> registerEntityRenderer(typeProvider: Supplier<EntityType<R>>, provider: EntityRendererProvider<R>)
	fun <R : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(typeProvider: Supplier<BlockEntityType<R>>, provider: BlockEntityRendererProvider<R, S>)
}