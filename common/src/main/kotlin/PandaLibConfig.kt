/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import dev.pandasystems.pandalib.config.Config
import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.ConfigCategory
import dev.pandasystems.pandalib.config.options.GenericConfigOption
import dev.pandasystems.pandalib.config.options.syncable
import dev.pandasystems.pandalib.utils.extensions.resourceLocation

class PandaLibConfig: Config() {
	var debugging = GenericConfigOption(false).syncable()
	var experimentalFeatures by GenericConfigOption(false)

	@ConfigCategory
	val hotReload = HotReloadConfig() //TODO: Implement hot reload functionality

	class HotReloadConfig {
		var enableConfigHotReload by GenericConfigOption(false)
		var configHotReloadDelay by GenericConfigOption(1_000L)
	}

	companion object {
		fun configSynchronizerClientInit() {
			TODO("Not yet implemented")
		}
	}
}

val pandalibConfig = ConfigRegistry.create(resourceLocation("pandalib_config"), ::PandaLibConfig)