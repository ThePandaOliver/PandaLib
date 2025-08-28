/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.serializers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import dev.pandasystems.pandalib.config.ConfigSerializer

class JsonConfigSerializer<T : Any>(mapper: ObjectMapper = ObjectMapper()) : ConfigSerializer<T> {
	val reader: ObjectReader = mapper.reader()
	val writer: ObjectWriter = mapper.writerWithDefaultPrettyPrinter()
	
	override fun serialize(config: T): String {
		return writer.writeValueAsString(config)
	}

	override fun deserialize(json: String, clazz: Class<T>): T {
		return reader.readValue(json, clazz)
	}
}