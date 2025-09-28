/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

import dev.pandasystems.pandalib.config.Config

class TomlConfigSerializer<T : Config>(
	val configClass: Class<T>
) : ConfigSerializer<T> {
	override fun serialize(config: T): String {
		TODO("Not yet implemented")
	}

	override fun deserialize(data: String, config: T) {
		TODO("Not yet implemented")
	}

	override fun deserialize(data: String): T {
		TODO("Not yet implemented")
	}

	override fun createDefault(): T {
		TODO("Not yet implemented")
	}

	override val fileExtension: String = "toml"
}