/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

abstract class ConfigProperty<T>(val name: String, val comment: String, val default: T) {
	var value: T = default

	internal lateinit var configParent: Configuration
	internal lateinit var menuParent: ConfigMenu

	fun resetToDefault() {
		value = default
	}

	fun createMapEntry() = object : Map.Entry<String, ConfigProperty<T>> {
		override val key: String = name
		override val value = this@ConfigProperty
	}
}