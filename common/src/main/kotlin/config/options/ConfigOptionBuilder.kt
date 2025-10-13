/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.common.reflect.TypeToken
import dev.pandasystems.pandalib.config.ConfigObject
import java.lang.reflect.Field
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.kotlinProperty

class ConfigOptionBuilder<T : Any?, C : ConfigOption<T>>(
	val value: T,
	val valueType: Class<T>,
	val constructor: (configObject: ConfigObject<*>, pathName: String, type: TypeToken<T>, value: T) -> C
) {
	lateinit var delegate: C

	fun lateInit(
		configObject: ConfigObject<*>, field: Field, path: String? = null
	) {
		val name = field.kotlinProperty?.name ?: field.name
		val path = path?.let { "$it.$name" } ?: name

		@Suppress("UNCHECKED_CAST")
		delegate = constructor(configObject, path, TypeToken.of(valueType) as TypeToken<T>, value)
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): C = delegate
}

fun <T: Any?> ConfigOptionBuilder<T, GenericConfigOption<T>>.syncable() : ConfigOptionBuilder<T, SyncableConfigOption<T>> {
	return ConfigOptionBuilder(this.value, this.valueType, ::SyncableConfigOption)
}

inline fun <reified T> configOption(value: T): ConfigOptionBuilder<T, GenericConfigOption<T>> {
	return ConfigOptionBuilder(value, T::class.java, ::GenericConfigOption)
}