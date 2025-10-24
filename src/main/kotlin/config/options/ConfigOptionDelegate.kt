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

package dev.pandasystems.pandalib.config.options

import dev.pandasystems.pandalib.config.ConfigObject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType

class ConfigOptionDelegate<T : Any>(
	private val defaultValue: T,
	private val type: KType,
	private val optionFactory: (ConfigObject<*>, String, KType, T) -> ConfigOption<T>
) : ReadOnlyProperty<Any?, ConfigOption<T>> {
    private var option: ConfigOption<T>? = null
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): ConfigOption<T> {
        return option ?: throw IllegalStateException("Config not initialized")
    }
    
    internal fun initialize(configObject: ConfigObject<*>, fullPath: String) {
        option = optionFactory(configObject, fullPath, type, defaultValue)
    }
}