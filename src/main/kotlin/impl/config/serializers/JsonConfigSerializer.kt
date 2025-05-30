package dev.pandasystems.pandalib.impl.config.serializers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import dev.pandasystems.pandalib.api.config.ConfigSerializer

class JsonConfigSerializer<T : Any>(mapper: ObjectMapper = ObjectMapper()) : ConfigSerializer<T> {
	val reader: ObjectReader = mapper.reader()
	val writer: ObjectWriter = mapper.writerWithDefaultPrettyPrinter()
	
	override fun serialize(config: T): String {
		return writer.writeValueAsString(config)
	}

	override fun deserialize(json: String): T {
		@Suppress("UNCHECKED_CAST")
		return reader.readValue(json, Any::class.java) as T
	}
}