/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config.options

import dev.pandasystems.pandalib.config.ConfigObject
import java.lang.reflect.Field
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.jvm.kotlinProperty
import kotlin.reflect.typeOf

class ConfigOptionBuilder<T : Any, C : ConfigOption<T>>(
	val value: T,
	val valueType: KType,
	val constructor: (configObject: ConfigObject<*>, pathName: String, type: KType, value: T) -> C
) {
	lateinit var delegate: C

	fun lateInit(
		configObject: ConfigObject<*>, field: Field, path: String? = null
	) {
		val name = field.kotlinProperty?.name ?: field.name
		val path = path?.let { "$it.$name" } ?: name

		@Suppress("UNCHECKED_CAST")
		delegate = constructor(configObject, path, valueType, value)
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): C = delegate
}

fun <T: Any> ConfigOptionBuilder<T, GenericConfigOption<T>>.syncable() : ConfigOptionBuilder<T, SyncableConfigOption<T>> {
	return ConfigOptionBuilder(this.value, this.valueType, ::SyncableConfigOption)
}

inline fun <reified T : Any> configOption(value: T): ConfigOptionBuilder<T, GenericConfigOption<T>> {
	return ConfigOptionBuilder(value, typeOf<T>(), ::GenericConfigOption)
}