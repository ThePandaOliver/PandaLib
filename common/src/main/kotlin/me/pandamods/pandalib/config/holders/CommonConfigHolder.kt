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

package me.pandamods.pandalib.config.holders

import me.pandamods.pandalib.config.Config
import me.pandamods.pandalib.config.ConfigData
import me.pandamods.pandalib.platform.Services

class CommonConfigHolder<T : ConfigData>(
	configClass: Class<T>,
	config: Config
) : ConfigHolder<T>(configClass, config) {
	private var commonConfig: T? = null
	
	fun setCommonConfig(config: T) {
		commonConfig = config
	}

	override fun get(): T? {
		if (Services.GAME.isClient && commonConfig != null) {
			return commonConfig
		}
		return super.get()
	}
}