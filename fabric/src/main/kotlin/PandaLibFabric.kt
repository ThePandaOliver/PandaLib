/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.event.commonevents.ServerStartingEvent
import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.event.EventListener
import net.fabricmc.api.ModInitializer
import net.minecraft.server.MinecraftServer

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		EventListener.add(::onServerStart)

		PandaLib // Initialize the core PandaLib functionality
	}

	private fun onServerStart(event: ServerStartingEvent) {
		server = event.server
	}

	companion object {
		var server: MinecraftServer? = null
	}
}
