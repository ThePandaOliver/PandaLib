/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.options

import com.google.gson.JsonElement
import dev.pandasystems.pandalib.config.ConfigObject
import org.spongepowered.asm.util.Annotations.setValue
import java.lang.reflect.Field
import java.util.function.Supplier
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinProperty

abstract class ConfigOption<T: Any> {
	var isInitialized = false
		private set

	lateinit var configObject: ConfigObject<*>
		internal set

	lateinit var name: String
		internal set
	lateinit var path: String
		internal set

	abstract var value: T
	val type: Class<T> get() = value.javaClass

	open fun initialize(
		configObject: ConfigObject<*>,
		path: String, name: String
	) {
		require(!isInitialized) { "ConfigOption $name is already initialized" }
		this.configObject = configObject

		this.name = name
		this.path = path

		isInitialized = true
	}

	abstract fun serialize(value: T): JsonElement
	abstract fun deserialize(element: JsonElement): T

	open fun getAndSerialize(): JsonElement = serialize(value)

	open fun deserializeAndSet(element: JsonElement) {
		value = deserialize(element)
	}
}