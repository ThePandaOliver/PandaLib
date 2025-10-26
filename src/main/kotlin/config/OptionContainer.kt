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

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.options.ConfigOption
import kotlin.reflect.KProperty

abstract class OptionContainer {
	private val _options = mutableListOf<ConfigOption<out Any?>>()
	private val _container = mutableListOf<OptionContainer>()

	val options: List<ConfigOption<out Any?>> get() = _options
	val container: List<OptionContainer> get() = _container

	lateinit var name: String
		private set
	var parent: OptionContainer? = null
		private set

	val path: String = if (parent?.path == null) name else "${parent!!.path}.$name"

	internal fun addOption(option: ConfigOption<out Any?>) {
		_options.add(option)
	}

	operator fun provideDelegate(thisRef: Any, property: KProperty<*>): OptionContainer {
		if (thisRef is OptionContainer) {
			thisRef._container.add(this)
			this.name = property.name
			this.parent = thisRef
		}
		return this
	}
}