/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.event.serverevents.serverStartingEvent
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents
import net.minecraft.server.MinecraftServer

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		serverStartingEvent.register { server = it }

		ServerConfigurationConnectionEvents.CONFIGURE.register { handler, server ->
			dev.pandasystems.pandalib.event.serverevents.ServerConfigurationConnectionEvents.configure.invoker().invoke(handler, server) }

		PandaLib // Initialize the core PandaLib functionality
	}

	companion object {
		var server: MinecraftServer? = null
	}
}
