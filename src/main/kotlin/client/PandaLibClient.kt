/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.client

//? if fabric {
import net.fabricmc.api.ClientModInitializer

//?} else if neoforge {
/*import net.neoforged.fml.common.Mod
*///?}

import dev.pandasystems.pandalib.client.config.ClientConfigSynchronizer
import dev.pandasystems.pandalib.logger

internal fun initializeClientMod() {
	logger.debug("PandaLib Client is initializing...")

	ClientConfigSynchronizer.init()

	logger.debug("PandaLib initialized successfully.")
}

//? if fabric {
class PandaLibClientFabric : ClientModInitializer {
	override fun onInitializeClient() {
		initializeClientMod()
	}
}
//?}

//? if neoforge {
/*@Mod(MOD_ID)
class PandaLibClientNeoForge {
	init {
		initializeClientMod()
	}
}
*///?}