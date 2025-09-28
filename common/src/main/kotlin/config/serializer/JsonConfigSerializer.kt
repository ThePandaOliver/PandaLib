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
import dev.pandasystems.pandalib.config.Config
import dev.pandasystems.pandalib.utils.constructClassUnsafely

class JsonConfigSerializer<T : Config>(
	val configClass: Class<T>,
	val gson: Gson = GsonBuilder().setPrettyPrinting().create()
) : ConfigSerializer<T> {
	override fun serialize(config: T): String {
		val json = JsonObject()
		config.options.forEach { property ->
			val path = property.name
			val name = path.substringAfterLast(".")
			computeJsonObjectPath(json, path).add(name, gson.toJsonTree(property.value))
		}
		return gson.toJson(json)
	}

	override fun deserialize(data: String, config: T) {
		val json = gson.fromJson(data, JsonObject::class.java)
		config.options.forEach { property ->
			val path = property.name
			val name = path.substringAfterLast(".")
			computeJsonObjectPath(json, path).get(name)?.let { propertyValue -> property.value = gson.fromJson(propertyValue, property.type) }
		}
	}

	fun computeJsonObjectPath(rootObject: JsonObject, path: String): JsonObject {
		val pathNames = path.split(".")
		val lastIndex = pathNames.size - 1
		var parentObject = rootObject

		pathNames.forEachIndexed { index, name ->
			if (index != lastIndex) {
				parentObject = parentObject.getAsJsonObject(name) // Get sub-object
					?: JsonObject().also { parentObject.add(name, it) } // Create if not present
			}
		}
		return parentObject
	}

	override fun createDefault(): T = configClass.constructClassUnsafely()

	override val fileExtension: String = "json"
}