/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import org.slf4j.Logger

@InternalPandaLibApi
fun initializePandaLib() {
	pandalibLogger.debug("PandaLib is initializing...")

	pandalibConfig.load()

	ConfigSynchronizer.init()

	pandalibLogger.debug("PandaLib initialized successfully.")
}

const val pandalibModid = "pandalib"
val pandalibLogger: Logger = LogUtils.getLogger()