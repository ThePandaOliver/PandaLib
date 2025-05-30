package dev.pandasystems.pandalib.impl.config.serializers

import dev.pandasystems.pandalib.api.config.ConfigSerializer

class JsonConfigSerializer<T : Any> : ConfigSerializer<T> {
	override fun serialize(config: T): String {
		TODO("Not yet implemented")
	}

	override fun deserialize(json: String, config: T) {
		TODO("Not yet implemented")
	}
}