/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config.options

import dev.pandasystems.pandalib.config.OptionContainer
import kotlin.reflect.KType

class GenericOption<T : Any?>(value: T, valueType: KType, name: String, parent: OptionContainer) : ConfigOption<T>(value, valueType, name, parent)

inline fun <reified T : Any?> OptionContainer.option(value: T): ConfigOptionDelegate<T, GenericOption<T>> =
	ConfigOptionDelegate(value, ::GenericOption)