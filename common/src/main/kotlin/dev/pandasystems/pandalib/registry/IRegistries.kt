package dev.pandasystems.pandalib.registry

import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

interface IRegistries {
	val blocks: DeferredRegistry<Block>
	val items: DeferredRegistry<Item>
	val entityTypes: DeferredRegistry<EntityType<*>>
}