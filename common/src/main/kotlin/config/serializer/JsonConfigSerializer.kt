/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.pandasystems.pandalib.utils.constructClassUnsafely

class JsonConfigSerializer<T : Any>(
	val configClass: Class<T>,
	val gson: Gson = GsonBuilder().setPrettyPrinting().create()
) : ConfigSerializer<T> {
	override fun serialize(config: T): String {
		return gson.toJson(config)
	}

	override fun deserialize(data: String): T {
		return gson.fromJson(data, configClass)
	}

	override fun createDefault(): T = configClass.constructClassUnsafely()

	override val fileExtension: String = "json"
}