/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config.holders

import dev.pandasystems.pandalib.config.Config
import dev.pandasystems.pandalib.config.ConfigData
import dev.pandasystems.pandalib.platform.Services

class CommonConfigHolder<T : ConfigData>(
	configClass: Class<T>,
	config: Config
) : ConfigHolder<T>(configClass, config) {
	private var commonConfig: T? = null
	
	fun <C : ConfigData> setCommonConfig(config: C) {
		@Suppress("UNCHECKED_CAST")
		commonConfig = config as T
	}

	override fun get(): T? {
		if (Services.GAME.isClient && commonConfig != null) {
			return commonConfig
		}
		return super.get()
	}
}