/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

import com.google.common.reflect.TypeToken
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import dev.pandasystems.pandalib.config.serializer.adapters.BooleanTypeAdapter
import dev.pandasystems.pandalib.config.serializer.adapters.ConfigSerializerTypeAdapter
import dev.pandasystems.pandalib.config.serializer.adapters.LongTypeAdapter
import dev.pandasystems.pandalib.utils.constructClassUnsafely
import java.lang.reflect.Type
import kotlin.reflect.jvm.kotlinProperty

object ConfigSerialization {
	private val adapters = mutableMapOf<Type, ConfigSerializerTypeAdapter<*>>(
		java.lang.Boolean::class.java to BooleanTypeAdapter(),
		java.lang.Long::class.java to LongTypeAdapter()
	)

	fun serialize(obj: Any?): JsonElement {
		if (obj == null) return JsonNull.INSTANCE
		if (obj is JsonElement) return obj

		val adapter = getAdapter(obj.javaClass)
		if (adapter != null)
			return adapter.serialize(obj)

		val jsonObject = JsonObject()
		obj.javaClass.declaredFields.forEach { field ->
			field.isAccessible = true

			val name = field.kotlinProperty?.name ?: field.name
			jsonObject.add(name, serialize(field.get(obj)))
		}
		return jsonObject
	}

	fun <T> deserialize(json: JsonElement, clazz: Class<T>): T? {
		return deserialize(json, TypeToken.of(clazz))
	}

	fun deserialize(json: JsonElement, type: Type): Any? {
		return deserialize(json, TypeToken.of(type))
	}

	fun <T> deserialize(json: JsonElement, typeToken: TypeToken<T>): T? {
		if (json.isJsonNull) return null
		val clazz = typeToken.rawType

		val adapter = getAdapter(clazz)
		if (adapter != null)
			@Suppress("UNCHECKED_CAST")
			return adapter.deserialize(json) as T?

		if (json.isJsonObject) {
			val obj = clazz.constructClassUnsafely()
			val jsonObject = json.asJsonObject

			clazz.declaredFields.forEach { field ->
				field.isAccessible = true

				val name = field.kotlinProperty?.name ?: field.name
				val fieldElement = jsonObject.get(name) ?: return@forEach
				val fieldType = field.type
				val deserialized = deserialize(fieldElement, fieldType) ?: return@forEach
				field.set(obj, deserialized)
			}

			@Suppress("UNCHECKED_CAST")
			return obj as T?
		}

		throw IllegalArgumentException("Cannot deserialize json element $json to type ${clazz.name}")
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> getAdapter(typeOf: Class<T>): ConfigSerializerTypeAdapter<T>? {
		return getAdapter(typeOf as Type) as ConfigSerializerTypeAdapter<T>?
	}

	@Suppress("UNCHECKED_CAST")
	fun getAdapter(typeOf: Type): ConfigSerializerTypeAdapter<*>? {
		return adapters[typeOf]
	}

	fun <T : Any> registerAdapter(clazz: Class<T>, adapter: ConfigSerializerTypeAdapter<T>) {
		adapters[clazz] = adapter
	}
}