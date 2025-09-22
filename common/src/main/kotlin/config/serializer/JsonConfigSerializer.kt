/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.pandasystems.pandalib.config.ConfigProperty
import dev.pandasystems.pandalib.config.ConfigStructElement

class JsonConfigSerializer : ConfigSerializer {
	private val gson = GsonBuilder().setPrettyPrinting().create()

	override fun serialize(configMap: Map<String, ConfigStructElement>): String {
		val jObject = JsonObject()

		configMap.forEach { entry ->
			if (entry.value.isProperty) {
				val prop = entry.value.property!!
				jObject.addProperty(prop.name, prop.value.toString())
			} else if (entry.value.isMenu) {
				val menuJson = serialize(entry.value.menu!!)
				jObject.add(entry.key, gson.fromJson(menuJson, JsonObject::class.java))
			}
		}

		return gson.toJson(jObject)
	}

	override fun deserialize(content: String, map: Map<String, ConfigStructElement>) {
		val jObject = gson.fromJson(content, JsonObject::class.java)

		map.forEach { entry ->
			if (entry.value.isProperty) {
				val prop = entry.value.property!! as ConfigProperty<Any?>
				if (jObject.has(prop.name)) {
					val jsonElement = jObject.get(prop.name)
					when (prop.default) {
						is Int -> prop.value = jsonElement.asInt
						is Boolean -> prop.value = jsonElement.asBoolean
						is Double -> prop.value = jsonElement.asDouble
						is Float -> prop.value = jsonElement.asFloat
						is Long -> prop.value = jsonElement.asLong
						is String -> prop.value = jsonElement.asString
						else -> throw IllegalArgumentException("Unsupported property type: ${prop.default!!::class.java}")
					}
				}
			} else if (entry.value.isMenu) {
				if (jObject.has(entry.key)) {
					val subObject = jObject.getAsJsonObject(entry.key)
					deserialize(gson.toJson(subObject), entry.value.menu!!)
				}
			}
		}
	}
}