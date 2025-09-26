/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import dev.pandasystems.pandalib.config.Option
import dev.pandasystems.pandalib.config.ConfigObject
import dev.pandasystems.pandalib.config.serializer.JsonConfigSerializer
import dev.pandasystems.pandalib.utils.extensions.resourceLocation

data class PandaLibConfig(
	@Option(comment = "Debugging mode enables more verbose logging and additional checks.")
	var debugging: Boolean = false,
	@Option(comment = "Enables experimental features that are not yet stable. Use with caution.")
	var experimentalFeatures: Boolean = false,

	val hotReload: HotReloadConfig = HotReloadConfig()
)

data class HotReloadConfig(
	@Option(comment = "Enables hot reloading of the configuration file when changes are detected. Might cause performance issues.")
	var enableConfigHotReload: Boolean = false,
	@Option(comment = "Delay in milliseconds between checks for configuration file changes when hot reload is enabled.")
	var configHotReloadDelay: Long = 1_000L,
)

val pandalibConfig = ConfigObject(resourceLocation("pandalib_config"), PandaLibConfig::class.java)