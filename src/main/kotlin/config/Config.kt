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

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.options.ConfigOption
import dev.pandasystems.pandalib.config.options.ConfigOptionBuilder
import kotlin.reflect.jvm.kotlinProperty

abstract class Config {
	private var isInitialized = false
	lateinit var options: List<ConfigOption<*>>
		internal set

	lateinit var configObject: ConfigObject<*>
		internal set

	internal fun lateInit(configObject: ConfigObject<*>) {
		if (isInitialized) throw IllegalStateException("Config $this is already initialized")
		val mutableOptions = mutableListOf<ConfigOption<*>>()

		fun Any.applyCategory(path: String? = null) {
			this::class.java.fields.forEach { field ->
				val name = field.kotlinProperty?.name ?: field.name
				if (ConfigOptionBuilder::class.java.isAssignableFrom(field.type)) {
					field.isAccessible = true

					val builder = field.get(this) as? ConfigOptionBuilder<*, *> ?: throw IllegalArgumentException("Field $field returns null")
					builder.lateInit(configObject, field, path)

					mutableOptions += builder.delegate
				} else if (field.isAnnotationPresent(ConfigCategory::class.java)) {
					field.isAccessible = true
					field.get(this).applyCategory(path?.let { "$it.$name" } ?: name)
				} // else: ignore other types
			}
		}
		applyCategory()

		options = mutableOptions.toList()
		this.configObject = configObject
		isInitialized = true
	}
}