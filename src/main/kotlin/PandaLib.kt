/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.config.syncOption
import dev.pandasystems.pandalib.utils.InternalPandaLibApi
import org.slf4j.Logger

@InternalPandaLibApi
fun initializePandaLib() {
	pandalibLogger.debug("PandaLib is initializing...")

	pandalibConfig.load()
	pandalibConfig.syncOption(PandaLibConfig::debugging)
	pandalibConfig.syncOption(PandaLibConfig.HotReloadConfig::configHotReloadDelay)
	pandalibConfig.syncOption(PandaLibConfig.HotReloadConfig::enableConfigHotReload)

	ConfigSynchronizer.init()

	pandalibLogger.debug("PandaLib initialized successfully.")
}

const val pandalibModid = "pandalib"
val pandalibLogger: Logger = LogUtils.getLogger()