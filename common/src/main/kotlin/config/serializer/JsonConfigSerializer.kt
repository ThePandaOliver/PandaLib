/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

import com.google.gson.GsonBuilder

/**
 * A JSON implementation of the ConfigSerializer interface using Gson.
 * > Comments in the JSON file are not supported.
 */
class JsonConfigSerializer<T> : ConfigSerializer<T> {
	private val gson = GsonBuilder().setPrettyPrinting().create()
	override val fileExtension: String = "json"

	override fun <T> serialize(config: T): String {
		return gson.toJson(config)
	}

	override fun <T> deserialize(data: String, configClass: Class<T>): T {
		return gson.fromJson(data, configClass)
	}
}