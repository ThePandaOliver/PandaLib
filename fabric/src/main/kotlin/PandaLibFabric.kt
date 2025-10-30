/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.event.server.serverConfigurationConnectionEvent
import dev.pandasystems.pandalib.event.server.serverStartingEvent
import dev.pandasystems.pandalib.event.server.serverStoppingEvent
import dev.pandasystems.pandalib.fabric.platform.GameEnvironmentImpl
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		serverStartingEvent.register { (gameEnvironment as GameEnvironmentImpl).server = it }
		serverStoppingEvent.register { (gameEnvironment as GameEnvironmentImpl).server = null }

		ServerConfigurationConnectionEvents.CONFIGURE.register { handler, server ->
			serverConfigurationConnectionEvent.invoker(handler, server)
		}

		PandaLib
	}
}
