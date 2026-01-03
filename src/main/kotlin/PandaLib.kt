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
import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.config.syncOption
import net.minecraft.resources.Identifier
import org.slf4j.Logger

object PandaLib {
	const val modid = "pandalib"
	val logger: Logger = LogUtils.getLogger()

	val config = ConfigRegistry.create(identifier("pandalib_config"), PandaLibConfig)

	init {
		logger.debug("PandaLib is initializing...")

		config.load()
		config.syncOption(PandaLibConfig::debugging)
		config.syncOption(PandaLibConfig.HotReloadConfig::configHotReloadDelay)
		config.syncOption(PandaLibConfig.HotReloadConfig::enableConfigHotReload)

		ConfigSynchronizer.init()

		logger.debug("PandaLib initialized successfully.")
	}

	@Deprecated("ResourceLocation renamed to Identifier", ReplaceWith("identifier(path)"))
	fun resourceLocation(path: String): Identifier = Identifier.fromNamespaceAndPath(modid, path)
	fun identifier(path: String): Identifier = Identifier.fromNamespaceAndPath(modid, path)
}