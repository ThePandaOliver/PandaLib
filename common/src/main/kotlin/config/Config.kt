/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.properties.ConfigProperty
import dev.pandasystems.pandalib.config.properties.DynamicConfigProperty
import dev.pandasystems.pandalib.utils.getFieldUnsafely
import kotlin.reflect.full.isSubclassOf

abstract class Config {
	private val _options = mutableListOf<ConfigProperty<*>>()
	val options: List<ConfigProperty<*>> get() = _options

	internal fun initialize() {
		fun initializeProperties(basePath: String, parent: Any) {
			parent::class.java.declaredFields.forEach { field ->
				field.isAccessible = true

				// Initialize option properties
				field.getAnnotation(Option::class.java)?.let { option ->
					val property: ConfigProperty<*> = if (field.type.kotlin.isSubclassOf(ConfigProperty::class)) {
						field.getFieldUnsafely(parent)
					} else {
						DynamicConfigProperty()
					}

					property.init(basePath + field.name, option, field, parent)
					_options.add(property)
					return@forEach
				}

				// If it's not a config option, then it must be a sub-config
				field.get(parent)?.let { subConfig ->
					initializeProperties("$basePath${field.name}.", subConfig)
				}
			}
		}
		initializeProperties("", this)
	}
}