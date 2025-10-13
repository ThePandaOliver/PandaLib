/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.event.client.clientBlockPlaceEvent
import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.server.serverBlockPlacePostEvent
import dev.pandasystems.pandalib.event.server.serverPlayerJoinEvent
import dev.pandasystems.pandalib.platform.game
import org.slf4j.Logger

object PandaLib {
	const val MOD_ID = "pandalib"

	init {
		logger.debug("PandaLib is initializing...")

		pandalibConfig.load()

		ConfigSynchronizer.init()

		logger.debug("PandaLib initialized successfully.")
	}
}

val logger: Logger = LogUtils.getLogger()