/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry

//? if fabric {
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
//?} elif neoforge {
/*import net.neoforged.neoforge.client.event.EntityRenderersEvent
*///?}

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

fun <R : Entity> registerEntityRenderer(typeProvider: Supplier<EntityType<R>>, provider: EntityRendererProvider<R>) {
	//? if fabric {
	EntityRenderers.register(typeProvider.get(), provider)
	//?} elif neoforge {
	/*@Suppress("UNCHECKED_CAST")
	entityRendererProviders[typeProvider as Supplier<EntityType<*>>] = provider
	*///?}
}

@JvmName("blockEntityRenderer")
fun <R : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(typeProvider: Supplier<BlockEntityType<R>>, provider: BlockEntityRendererProvider<R, S>) {
	//? if fabric {
	BlockEntityRenderers.register(typeProvider.get(), provider)
	//?} elif neoforge {
	/*@Suppress("UNCHECKED_CAST")
	blockEntityRendererProviders[typeProvider as Supplier<BlockEntityType<*>>] = provider
	*///?}
}

//? if neoforge {
/*private val entityRendererProviders = mutableMapOf<Supplier<EntityType<*>>, EntityRendererProvider<*>>()
private val blockEntityRendererProviders = mutableMapOf<Supplier<BlockEntityType<*>>, BlockEntityRendererProvider<*, *>>()

@Suppress("UNCHECKED_CAST")
internal fun onEntityRendererRegistryEvent(event: EntityRenderersEvent.RegisterRenderers) {
	entityRendererProviders.forEach { event.registerEntityRenderer(it.key.get(), it.value as EntityRendererProvider<in Entity>) }
	blockEntityRendererProviders.forEach { event.registerBlockEntityRenderer(it.key.get(), it.value as BlockEntityRendererProvider<in BlockEntity, in BlockEntityRenderState>) }
}
*///?}