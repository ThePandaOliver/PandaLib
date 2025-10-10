/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.options.ConfigOption
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinProperty

abstract class Config {
	private var isInitialized = false
	lateinit var options: List<ConfigOption<*>>
		internal set

	lateinit var configObject: ConfigObject<*>
		internal set

	internal fun initialize(configObject: ConfigObject<*>) {
		if (isInitialized) throw IllegalStateException("Config $this is already initialized")
		val mutableOptions = mutableListOf<ConfigOption<*>>()

		fun Any.applyCategory(path: String? = null) {
			this::class.java.declaredFields.forEach { field ->
				val name = field.kotlinProperty?.let { property ->
					property.isAccessible = true
					return@let property.name
				} ?: field.name
				val path = path?.let { "$it.$name" } ?: name

				if (ConfigOption::class.java.isAssignableFrom(field.type)) {
					field.isAccessible = true

					val option = field.get(this) as? ConfigOption<*> ?: throw IllegalArgumentException("Field $field returns null")
					option.initialize(
						configObject = configObject,
						path = path,
						name = name
					)

					mutableOptions += option
				} else if (field.isAnnotationPresent(ConfigCategory::class.java)) {
					field.isAccessible = true
					field.get(this).applyCategory(path)
				} // else: ignore other types
			}
		}
		applyCategory()

		options = mutableOptions.toList()
		this.configObject = configObject
		isInitialized = true
	}
}