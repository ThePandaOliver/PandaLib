/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

interface ConfigSerializer<T> {
	val fileExtension: String

	fun <T> serialize(config: T): String
	fun <T> deserialize(data: String, configClass: Class<T>): T
}

inline fun <reified T> ConfigSerializer<T>.deserialize(data: String): T {
	return deserialize(data, T::class.java)
}