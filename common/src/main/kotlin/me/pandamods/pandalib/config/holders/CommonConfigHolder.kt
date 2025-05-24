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

import dev.architectury.platform.Platform
import dev.architectury.utils.Env
import me.pandamods.pandalib.config.Config
import me.pandamods.pandalib.config.ConfigData

class CommonConfigHolder<T : ConfigData>(
    configClass: Class<T>,
    config: Config
) : ConfigHolder<T>(configClass, config) {
    
    private var commonConfig: T? = null

    @Suppress("UNCHECKED_CAST")
    fun <C : ConfigData> setCommonConfig(config: C) {
        this.commonConfig = config as T
    }

    override fun get(): T? {
        if (Platform.getEnvironment() == Env.CLIENT && this.commonConfig != null) {
            return this.commonConfig!!
        }
        return super.get()
    }
}