/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test

import dev.pandasystems.pandalib.event.commonevents.BlockBreakEvent
import dev.pandasystems.pandalib.event.commonevents.BlockPlaceEvent
import dev.pandasystems.pandalib.event.commonevents.ServerPlayerRespawnEvent
import dev.pandasystems.pandalib.event.commonevents.ServerPlayerWorldChangeEvent
import dev.pandasystems.pandalib.registry.registerEntityRenderer
import dev.pandasystems.pandalib.test.entities.HelloEntityRenderer
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object PandaLibTest {
	fun initializeCommonTest() {
		TestRegistry.itemRegister.register()
		TestRegistry.blockRegister.register()
		TestRegistry.entityRegister.register()


		// Events
		EventListener.add(::beforePlayerSwitchDimension)
		EventListener.add(::afterPlayerSwitchDimension)
		EventListener.add(::onPlayerRespawn)

		EventListener.add(::beforeBlockBreak)
		EventListener.add(::afterBlockBreak)
		EventListener.add(::beforeBlockPlace)
		EventListener.add(::afterBlockPlace)

		// Renderers
		registerEntityRenderer(TestRegistry.helloEntity) { HelloEntityRenderer(it) }
	}

	private fun beforePlayerSwitchDimension(event: ServerPlayerWorldChangeEvent.Pre) {
		if (event.player.inventory.contains(TestRegistry.helloItem.get().defaultInstance)) {
			event.cancelled = true
			event.player.sendSystemMessage(Component.literal("You can't switch dimensions with this item in your inventory!"))
		}
	}

	private fun afterPlayerSwitchDimension(event: ServerPlayerWorldChangeEvent.Post) {
		event.player.sendSystemMessage(Component.literal("You switched dimensions!"))
	}

	private fun onPlayerRespawn(event: ServerPlayerRespawnEvent) {
		event.newPlayer.sendSystemMessage(Component.literal("You respawned!"))
	}

	private fun beforeBlockBreak(event: BlockBreakEvent.Pre) {
		if (event.entity is ServerPlayer) {
			val player: ServerPlayer = event.entity
			if (player.inventory.contains(TestRegistry.helloItem.get().defaultInstance)) {
				event.cancelled = true
				player.sendSystemMessage(Component.literal("You can't break blocks with this item in your inventory!"))
			}
		}
	}

	private fun afterBlockBreak(event: BlockBreakEvent.Post) {
		if (event.entity is ServerPlayer) {
			val player: ServerPlayer = event.entity
			player.sendSystemMessage(Component.literal("You broke a block!"))
		}
	}

	private fun beforeBlockPlace(event: BlockPlaceEvent.Pre) {
		if (event.entity is ServerPlayer) {
			val player: ServerPlayer = event.entity
			if (player.inventory.contains(TestRegistry.helloItem.get().defaultInstance)) {
				event.cancelled = true
				player.sendSystemMessage(Component.literal("You can't place blocks with this item in your inventory!"))
			}
		}
	}

	private fun afterBlockPlace(event: BlockPlaceEvent.Post) {
		if (event.entity is ServerPlayer) {
			val player: ServerPlayer = event.entity
			player.sendSystemMessage(Component.literal("You placed a block!"))
		}
	}
}