package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.full.findAnnotations

object ConfigRegistry {
	val configHolders = mutableMapOf<ResourceLocation, ConfigHolder<*>>()

	fun <T : Any> register(configInstance: T): ConfigHolder<T> {
		val configAnno = configInstance::class.findAnnotations(Configuration::class).firstOrNull()
			?: throw IllegalArgumentException("Config instance must be annotated with @Configuration!")
		val id = ResourceLocation.fromNamespaceAndPath(configAnno.modId, configAnno.pathName)
		return ConfigHolderImpl(configAnno, configInstance).also { configHolders[id] = it }
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Any> get(id: ResourceLocation): ConfigHolder<T> = configHolders[id] as ConfigHolder<T>

	@Suppress("UNCHECKED_CAST")
	inline fun <reified T : Any> get(): ConfigHolder<T> = configHolders.values.first { it.config is T } as ConfigHolder<T>
}