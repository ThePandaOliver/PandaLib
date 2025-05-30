package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.impl.config.serializers.JsonConfigSerializer
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.full.findAnnotations

object ConfigRegistry {
	private val configHolders = mutableMapOf<ResourceLocation, ConfigHolder<*>>()

	/**
	 * Registers a configuration instance for the application.
	 * The provided instance must be annotated with [Configuration], giving details
	 * about the mod ID and configuration storage path.
	 */
	@JvmStatic
	fun <T : Any> register(configInstance: T): ConfigHolder<T> {
		val configAnno = configInstance::class.findAnnotations(Configuration::class).firstOrNull()
			?: throw IllegalArgumentException("Config instance must be annotated with @Configuration!")
		
		return ConfigHolderImpl(configAnno, configInstance, JsonConfigSerializer())
			.also { configHolders[it.id] = it }
			.also { it.reload() }
	}

	/**
	 * Retrieves the configuration holder associated with the given resource location.
	 */
	@JvmStatic
	@Suppress("UNCHECKED_CAST")
	fun <T : Any> get(id: ResourceLocation): ConfigHolder<T> = configHolders[id] as ConfigHolder<T>

	/**
	 * Retrieves the configuration holder associated with the given class [T].
	 */
	@Suppress("UNCHECKED_CAST")
	inline fun <reified T : Any> get(): ConfigHolder<T> = get(T::class.java)

	/**
	 * Retrieves the first configuration holder of the specified class [clazz].
	 */
	@JvmStatic
	@Suppress("UNCHECKED_CAST")
	fun <T : Any> get(clazz: Class<T>): ConfigHolder<T> =
		configHolders.values.first { clazz.arrayType().isInstance(it.config) } as ConfigHolder<T>
}