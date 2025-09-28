/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

import dev.pandasystems.pandalib.config.Config

interface ConfigSerializer<T : Config> {
	fun serialize(config: T): String
	fun deserialize(data: String, config: T)
	@Deprecated("")
	fun deserialize(data: String): T {
		throw UnsupportedOperationException("Deserializing from a string is not supported by this serializer.")
	}

	fun createDefault(): T

	val fileExtension: String
}