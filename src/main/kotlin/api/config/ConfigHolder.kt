package dev.pandasystems.pandalib.api.config

interface ConfigHolder<T : Any> {
	val config: T

	fun reload()
	fun save()
	fun resetToDefault()
}