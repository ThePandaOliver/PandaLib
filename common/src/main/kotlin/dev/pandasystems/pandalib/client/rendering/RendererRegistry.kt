package dev.pandasystems.pandalib.client.rendering

import dev.pandasystems.pandalib.core.utils.loadService
import dev.pandasystems.pandalib.registry.DeferredObject
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

interface RendererRegistry {
    fun <T : Entity> registerEntityRenderer(
        type: () -> EntityType<out T>,
        provider: EntityRendererProvider<T>
    )

    fun <T : Entity> registerEntityRenderer(
	    type: DeferredObject<EntityType<out T>>,
	    provider: EntityRendererProvider<T>
    ) = registerEntityRenderer({ type.get() }, provider)

    fun <T : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
        type: () -> BlockEntityType<out T>,
        provider: BlockEntityRendererProvider<T, S>
    )

    fun <T : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
	    type: DeferredObject<BlockEntityType<out T>>,
	    provider: BlockEntityRendererProvider<T, S>
    ) = registerBlockEntityRenderer({ type.get() }, provider)

    companion object : RendererRegistry by loadService()
}