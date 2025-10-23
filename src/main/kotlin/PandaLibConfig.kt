/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
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

import dev.pandasystems.pandalib.config.Config
import dev.pandasystems.pandalib.config.ConfigCategory
import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.options.configOption
import dev.pandasystems.pandalib.config.options.syncable
import dev.pandasystems.pandalib.utils.extensions.resourceLocation

class PandaLibConfig: Config() {
	val debugging by configOption(false).syncable()
	val experimentalFeatures by configOption(false)

	@ConfigCategory
	val hotReload = HotReloadConfig() //TODO: Implement hot reload functionality

	class HotReloadConfig {
		val enableConfigHotReload by configOption(false)
		val configHotReloadDelay by configOption(1_000L)
	}
}

val pandalibConfig = ConfigRegistry.create(resourceLocation("pandalib_config"), ::PandaLibConfig)