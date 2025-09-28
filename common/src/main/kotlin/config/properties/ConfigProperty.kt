/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.properties

import dev.pandasystems.pandalib.config.Option
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.function.Supplier

abstract class ConfigProperty<T> : Supplier<T> {
	lateinit var name: String
		internal set
	lateinit var optionData: Option
		internal set

	lateinit var field: Field
		internal set
	lateinit var parent: Any
		internal set

	abstract var value: T

	@Suppress("UNCHECKED_CAST")
	val type: Type get() = this.field.genericType


	override fun get(): T = value

	fun onSave() {}
	fun onLoad() {}

	internal fun init(name: String, optionData: Option, field: Field, parent: Any) {
		this.name = name
		this.optionData = optionData
		this.field = field
		this.parent = parent
	}
}