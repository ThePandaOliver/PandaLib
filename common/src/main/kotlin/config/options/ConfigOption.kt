/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import java.lang.reflect.Field
import java.util.function.Supplier
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinProperty

open class ConfigOption<T>(private var value: T) : Supplier<T> {
	lateinit var fieldParent: Any
		internal set
	lateinit var field: Field
		internal set

	val name: String by lazy {
		field.kotlinProperty?.let { property ->
			property.isAccessible = true
			return@let property.name
		} ?: field.name
	}

	override fun get(): T = value

	fun set(newValue: T) {
		value = newValue
	}

	fun serialize(): JsonElement {
		return when (value) {
			is Boolean -> JsonPrimitive(value as Boolean?)
			is Number -> JsonPrimitive(value as Number?)
			is String -> JsonPrimitive(value as String?)
			else -> throw IllegalArgumentException("Cannot serialize unknown type: ${value!!::class.qualifiedName}")
		}
	}

	@Suppress("UNCHECKED_CAST")
	fun deserialize(element: JsonElement) {
		value = when (value) {
			is Boolean -> element.asBoolean
			is Number -> element.asNumber
			is String -> element.asString
			else -> throw IllegalArgumentException("Cannot deserialize unknown type: ${value!!::class.qualifiedName}")
		} as T
	}

	operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) { value = newValue }
	operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
}