/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.serializer.serializers

import dev.pandasystems.pandalib.serializer.JsonElement
import dev.pandasystems.pandalib.serializer.JsonNull
import dev.pandasystems.pandalib.serializer.JsonObject
import dev.pandasystems.pandalib.serializer.NotSerializable
import dev.pandasystems.pandalib.serializer.PropertySerializerRegistry
import dev.pandasystems.pandalib.serializer.SerializationOverrides
import dev.pandasystems.pandalib.serializer.TypeSerializerContext
import dev.pandasystems.pandalib.serializer.resolver.TypeSerializerResolver
import dev.pandasystems.pandalib.serializer.providers.GlobalTypeSerializers
import dev.pandasystems.pandalib.serializer.providers.TypeSerializerProvider
import dev.pandasystems.pandalib.serializer.providers.TypeSerializerProviderImpl
import dev.pandasystems.pandalib.serializer.resolver.DefaultTypeSerializerResolver
import dev.pandasystems.pandalib.serializer.typeserializers.TypeSerializer
import kotlin.getValue
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

abstract class Serializer<Value : Any> : TypeSerializerProvider by TypeSerializerProviderImpl() {
	protected open val resolver: TypeSerializerResolver by lazy {
		DefaultTypeSerializerResolver(
			localProvider = this,
			globalProvider = GlobalTypeSerializers,
		)
	}
	
	// Codec
	abstract fun decode(value: Value): JsonElement
	abstract fun encode(json: JsonElement): Value
	
	// Serializer
	fun <Obj : Any> jsonToObject(json: JsonElement, dist: Obj): Obj {
		val jsonObject = json as? JsonObject
			?: throw IllegalArgumentException("Expected JsonObject for ${dist::class.qualifiedName}, got ${json::class.simpleName}")

		val propertySerializers = serializersFor(dist)

		dist::class.memberProperties.forEach { property ->
			if (property.isNotSerializable()) {
				return@forEach
			}

			val jsonValue = jsonObject[property.name] ?: return@forEach

			if (property !is KMutableProperty1<Obj, *>) {
				return@forEach
			}

			property.isAccessible = true

			try {
				if (jsonValue is JsonNull) {
					if (property.returnType.isMarkedNullable) {
						setPropertyValue(property, dist, null)
					}

					return@forEach
				}

				val serializer = findPropertySerializer(
					property = property,
					owner = dist,
					propertySerializers = propertySerializers
				)

				val value = if (serializer != null) {
					deserializeWith(serializer, jsonValue)
				} else {
					val currentValue = property.get(dist) ?: throw IllegalStateException(
						"No serializer found for property '${property.name}' and cannot recursively deserialize because current value is null"
					)

					jsonToObject(jsonValue, currentValue)
				}

				setPropertyValue(property, dist, value)
			} finally {
				property.isAccessible = false
			}
		}

		return dist
	}
	
	fun <Obj : Any> objectToJson(obj: Obj): JsonElement {
		val jsonObject = JsonObject()
		val propertySerializers = serializersFor(obj)

		obj::class.memberProperties.forEach { property ->
			if (property.isNotSerializable()) {
				return@forEach
			}

			property.isAccessible = true

			try {
				val value = property.getter.call(obj)

				val jsonValue = if (value == null) {
					JsonNull
				} else {
					val serializer = findPropertySerializer(
						property = property,
						owner = obj,
						propertySerializers = propertySerializers
					)

					if (serializer != null) {
						serializeWith(serializer, value)
					} else {
						objectToJson(value)
					}
				}

				jsonObject[property.name] = jsonValue
			} finally {
				property.isAccessible = false
			}
		}

		return jsonObject
	}

	private fun serializersFor(obj: Any): PropertySerializerRegistry {
		val registry = PropertySerializerRegistry()

		if (obj is SerializationOverrides) {
			obj.defineSerializers(registry)
		}

		return registry
	}

	private fun findPropertySerializer(
		property: KProperty1<out Any, *>,
		owner: Any,
		propertySerializers: PropertySerializerRegistry
	): TypeSerializer<*>? {
		propertySerializers.find(property.name)?.let {
			return it
		}

		val rawType = property.returnType.jvmErasure.java
		val annotations = property.annotations + listOfNotNull(property.javaField).flatMap { it.annotations.toList() }

		return resolver.resolve(
			TypeSerializerContext(
				rawType = rawType,
				annotations = annotations,
				kotlinType = property.returnType,
				javaType = property.javaField?.genericType
			)
		)
	}

	private fun KProperty1<out Any, *>.isNotSerializable(): Boolean {
		return findAnnotation<NotSerializable>() != null ||
				javaField?.getAnnotation(NotSerializable::class.java) != null
	}

	@Suppress("UNCHECKED_CAST")
	private fun serializeWith(serializer: TypeSerializer<*>, value: Any): JsonElement {
		return (serializer as TypeSerializer<Any>).serialize(value)
	}

	@Suppress("UNCHECKED_CAST")
	private fun deserializeWith(serializer: TypeSerializer<*>, json: JsonElement): Any {
		return (serializer as TypeSerializer<Any>).deserialize(json)
	}

	@Suppress("UNCHECKED_CAST")
	private fun <Obj : Any> setPropertyValue(
		property: KMutableProperty1<Obj, *>,
		obj: Obj,
		value: Any?
	) {
		(property as KMutableProperty1<Obj, Any?>).set(obj, value)
	}
	
	// Extras
	fun <Obj : Any> decodeToObject(value: Value, dist: Obj): Obj = jsonToObject(decode(value), dist)
	fun <Obj : Any> encodeObject(obj: Obj): Value = encode(objectToJson(obj))
}