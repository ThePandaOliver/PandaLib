/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializer

interface ConfigSerializer<T : Any> {
	fun serialize(config: T): String
	fun deserialize(data: String): T

	fun createDefault(): T

	val fileExtension: String
}