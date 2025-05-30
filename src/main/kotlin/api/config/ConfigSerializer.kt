package dev.pandasystems.pandalib.api.config

interface ConfigSerializer<T : Any> {
	fun serialize(config: T): String
	fun deserialize(json: String, clazz: Class<T>): T
}