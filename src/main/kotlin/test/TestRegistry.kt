/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test

import dev.pandasystems.pandalib.core.PandaLib
import dev.pandasystems.pandalib.impl.registry.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

object TestRegistry {
	val itemRegister = DeferredRegister.create(PandaLib.MOD_ID, Registries.ITEM)
	val blockRegister = DeferredRegister.create(PandaLib.MOD_ID, Registries.BLOCK)


	// Item Registry

	val helloItem = itemRegister.register("hello_item", registryFunc = { Item(Item.Properties().setId(it)) })


	// Block Registry

	val helloBlock = blockRegister.register("hello_block", registrySup = { Block(BlockBehaviour.Properties.of()) })
}