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
import kotlin.reflect.KProperty
import kotlin.reflect.KType

class ConfigOptionDelegate<T : Any?, R: ConfigOption<T>>(
	val defaultValue: T,
	val optionFactory: (value: T, valueType: KType, name: String, parent: OptionContainer) -> R
) {
	operator fun provideDelegate(thisRef: OptionContainer, property: KProperty<*>): R {
		return optionFactory(defaultValue, property.returnType, property.name, thisRef)
			.also { thisRef.addOption(it) }
	}
}