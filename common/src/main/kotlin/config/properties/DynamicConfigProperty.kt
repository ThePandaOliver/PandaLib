/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.properties

class DynamicConfigProperty : ConfigProperty<Any?>() {
	override var value: Any?
		@Suppress("UNCHECKED_CAST")
		get() = this.field.get(parent)
		set(value) = this.field.set(parent, value)
}