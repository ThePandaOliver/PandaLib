/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.event.server.serverStartingEvent
import dev.pandasystems.pandalib.event.server.serverStoppingEvent
import dev.pandasystems.pandalib.fabric.platform.GameEnvironmentImpl
import dev.pandasystems.pandalib.initializePandaLib
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.fabricmc.api.ModInitializer

@OptIn(InternalPandaLibApi::class)
class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		serverStartingEvent.register { (gameEnvironment as GameEnvironmentImpl).server = it }
		serverStoppingEvent.register { (gameEnvironment as GameEnvironmentImpl).server = null }

		initializePandaLib()
	}
}
