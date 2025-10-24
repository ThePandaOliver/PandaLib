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

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.datatypes.ConfigDataType
import dev.pandasystems.universalserializer.elements.TreeObject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

abstract class Config {
	private val options = mutableListOf<Option<*>>()

	abstract val version: Int
	open fun migrate(oldVersion: Int, data: TreeObject) {}

	internal fun <T : Config> initialize(configObject: ConfigObject<T>) {
		fun iterateProperties(memberProperties: Collection<KProperty<*>>, instance: Any) {
			for (property in memberProperties) {
				if (property !is KMutableProperty<*>) continue
				property.isAccessible = true
				val returnType = property.returnType

				@Suppress("UNCHECKED_CAST")
				val dataType = returnType.classifier as? ConfigDataType<Any?> ?: object : ConfigDataType<Any?> {
					override var value: Any?
						get() = property.getter.call(instance)
						set(value) {
							property.setter.call(instance, value)
						}
					override val valueType: KType
						get() = returnType

				}

				options += Option(property.name, dataType, returnType)
			}
		}

		val klass = this::class
		iterateProperties(klass.memberProperties, klass.objectInstance ?: this)
	}

	inner class Option<T>(
		val name: String,
		val dataType: ConfigDataType<T>,
		val optionType: KType
	) : ConfigDataType<T> by dataType {
		val config: Config
			get() = this@Config
	}
}