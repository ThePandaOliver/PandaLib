/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.core

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.impl.config.ConfigRegistry
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger


object PandaLib {
	const val MOD_ID = "pandalib"
	
	val config = ConfigRegistry.register<PandaLibConfig>()
	
	init {
		logger.debug("PandaLib Core is initializing...")

		logger.debug("PandaLib Core initialized successfully.")
	}
	
	@JvmStatic
	fun resourceLocation(path: String): ResourceLocation {
		return dev.pandasystems.pandalib.utils.extensions.resourceLocation(MOD_ID, path)
	}
}

val logger: Logger = LogUtils.getLogger()