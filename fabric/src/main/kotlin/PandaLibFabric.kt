/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.api.event.commonevents.PlayerJoinEvent
import dev.pandasystems.pandalib.api.event.invokeEvent
import dev.pandasystems.pandalib.core.PandaLib
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register { server: MinecraftServer? -> Companion.server = server }

		PandaLib // Initialize the core PandaLib functionality

		ServerPlayerEvents.JOIN.register { player ->
			invokeEvent(PlayerJoinEvent(player))
		}
	}

	companion object {
		var server: MinecraftServer? = null
	}
}
