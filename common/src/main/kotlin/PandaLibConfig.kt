/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import dev.pandasystems.pandalib.config.Config
import dev.pandasystems.pandalib.config.ConfigObject
import dev.pandasystems.pandalib.config.options.ConfigOption
import dev.pandasystems.pandalib.utils.extensions.resourceLocation

class PandaLibConfig: Config() {
	var debugging by ConfigOption(false)
	var experimentalFeatures by ConfigOption(false)

	val hotReload = HotReloadConfig() //TODO: Implement hot reload functionality

	class HotReloadConfig: Config() {
		var enableConfigHotReload by ConfigOption(false)
		var configHotReloadDelay by ConfigOption(1_000L)
	}
}

val pandalibConfig = ConfigObject(resourceLocation("pandalib_config"), PandaLibConfig::class.java)