/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.impl.config.serializers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.pandasystems.pandalib.api.config.ConfigSerializer

class JsonConfigSerializer<T : Any>(private val gson: Gson = GsonBuilder().setPrettyPrinting().create()) : ConfigSerializer<T> {
	override fun serialize(config: T): String {
		return gson.toJson(config)
	}

	override fun deserialize(json: String, clazz: Class<T>): T {
		return gson.fromJson(json, clazz)
	}
}