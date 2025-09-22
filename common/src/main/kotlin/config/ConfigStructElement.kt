/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

class ConfigStructElement(val property: ConfigProperty<*>? = null, val menu: Map<String, ConfigStructElement>? = null) {
	val isProperty = property != null
	val isMenu = menu != null

	init {
		require(isProperty || isMenu) { "ConfigStructElement must be either a property or a menu" }
	}
}