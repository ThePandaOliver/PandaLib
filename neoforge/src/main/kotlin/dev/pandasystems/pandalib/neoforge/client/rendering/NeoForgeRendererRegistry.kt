package dev.pandasystems.pandalib.neoforge.client.rendering

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.client.rendering.RendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@AutoService(RendererRegistry::class)
class NeoForgeRendererRegistry : RendererRegistry {
    override fun <T : Entity> registerEntityRenderer(
        type: () -> EntityType<out T>,
        provider: EntityRendererProvider<T>
    ) {
        enqueue { event ->
            event.registerEntityRenderer(type(), provider)
        }
    }

    override fun <T : BlockEntity, S : BlockEntityRenderState> registerBlockEntityRenderer(
        type: () -> BlockEntityType<out T>,
        provider: BlockEntityRendererProvider<T, S>
    ) {
        enqueue { event ->
            event.registerBlockEntityRenderer(type(), provider)
        }
    }

    companion object {
        private val queuedActions = mutableListOf<(EntityRenderersEvent.RegisterRenderers) -> Unit>()

        private fun enqueue(action: (EntityRenderersEvent.RegisterRenderers) -> Unit) {
            synchronized(queuedActions) {
                queuedActions.add(action)
            }
        }

        internal fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
            synchronized(queuedActions) {
                queuedActions.forEach { it(event) }
                queuedActions.clear()
            }
        }
    }
}