/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.registry.deferred.DeferredRegister
import dev.pandasystems.pandalib.test.entities.HelloEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

object TestRegistry {
	val itemRegister = DeferredRegister.create(PandaLib.MOD_ID, Registries.ITEM)
	val blockRegister = DeferredRegister.create(PandaLib.MOD_ID, Registries.BLOCK)
	val entityRegister = DeferredRegister.create(PandaLib.MOD_ID, Registries.ENTITY_TYPE)


	// Item Registry

	val helloItem = itemRegister.register("hello_item") { Item(Item.Properties().setId(it)) }


	// Block Registry

	val helloBlock = blockRegister.register("hello_block") { Block(BlockBehaviour.Properties.of().setId(it)) }


	// Entity Registry

	val helloEntity = entityRegister.register("hello_entity") { EntityType.Builder.of(::HelloEntity, MobCategory.MISC).build(it) }
}