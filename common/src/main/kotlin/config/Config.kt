/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.options.ConfigOption

abstract class Config {
	private var isInitialized = false
	lateinit var options: List<ConfigOption<*>>
	lateinit var subCategories: Map<String, Config>

	internal fun initialize() {
		if (isInitialized) throw IllegalStateException("Config $this is already initialized")
		isInitialized = true
		val mutableOptions = mutableListOf<ConfigOption<*>>()
		val mutableSubCategories = mutableMapOf<String, Config>()

		javaClass.declaredFields.forEach {
			if (ConfigOption::class.java.isAssignableFrom(it.type)) {
				it.isAccessible = true

				val option = it.get(this) as? ConfigOption<*> ?: throw IllegalArgumentException("Field $it returns null")
				option.fieldParent = this
				option.field = it

				mutableOptions += option
			} else if (Config::class.java.isAssignableFrom(it.type)) {
				it.isAccessible = true
				val subCategory = it.get(this) as Config
				subCategory.initialize()
				mutableSubCategories[it.name] = subCategory
			} // else: ignore other types
		}

		options = mutableOptions.toList()
		subCategories = mutableSubCategories.toMap()
	}
}