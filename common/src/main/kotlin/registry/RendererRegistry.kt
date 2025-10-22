/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
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