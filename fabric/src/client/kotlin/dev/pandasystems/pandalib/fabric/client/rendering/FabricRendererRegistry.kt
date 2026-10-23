package dev.pandasystems.pandalib.fabric.client.rendering

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.client.rendering.RendererRegistry
import dev.pandasystems.pandalib.registry.DeferredObject
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@AutoService(RendererRegistry::class)
class FabricRendererRegistry : RendererRegistry {
    override fun <T : Entity> registerEntityRenderer(
        type: () -> EntityType<out T>,
        provider: EntityRendererProvider<T>
    ) {
        EntityRenderers.register(type(), provider)
    }

    override fun <T : Entity> registerEntityRenderer(
	    type: DeferredObject<EntityType<out T>>,
	    provider: EntityRendererProvider<T>
    ) {
        type.onBind { boundType ->
            EntityRenderers.register(boundType, provider)
        }
    }

    override fun <T : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
        type: () -> BlockEntityType<out T>,
        provider: BlockEntityRendererProvider<T, S>
    ) {
        BlockEntityRenderers.register(type(), provider)
    }

    override fun <T : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
	    type: DeferredObject<BlockEntityType<out T>>,
	    provider: BlockEntityRendererProvider<T, S>
    ) {
        type.onBind { boundType ->
            BlockEntityRenderers.register(boundType, provider)
        }
    }
}