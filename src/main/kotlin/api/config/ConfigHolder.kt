package dev.pandasystems.pandalib.api.config

import net.minecraft.resources.ResourceLocation

interface ConfigHolder<T : Any> {
	val config: T
	val id: ResourceLocation

	fun reload()
	fun save()
	fun resetToDefault()
}