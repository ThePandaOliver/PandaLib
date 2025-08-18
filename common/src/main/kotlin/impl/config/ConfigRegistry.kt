/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.core.logger
import dev.pandasystems.pandalib.impl.config.serializers.JsonConfigSerializer
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.full.findAnnotations

object ConfigRegistry {
	private val configHolders = mutableMapOf<ResourceLocation, ConfigHolder<*>>()

	/**
	 * Registers a configuration instance for the application.
	 *
	 * @param configInstance The provided instance must be annotated with [Configuration], giving details
	 * about the mod ID and configuration storage path.
	 */
	@JvmStatic
	fun <T : Any> register(configInstance: Class<T>): ConfigHolder<T> {
		val configAnno = configInstance.kotlin.findAnnotations(Configuration::class).firstOrNull()
			?: throw IllegalArgumentException("Config instance must be annotated with @Configuration!")

		return register(ConfigHolderImpl(configAnno, configInstance, JsonConfigSerializer()))
	}

	inline fun <reified T : Any> register(): ConfigHolder<T> = register(T::class.java)

	/**
	 * Registers a configuration holder for the application.
	 */
	@JvmStatic
	fun <T : Any> register(holder: ConfigHolder<T>): ConfigHolder<T> {
		if (configHolders.containsKey(holder.id))
			logger.warn("Config holder with id {} is already registered, replacing it.", holder.id)

		return holder.also {
			configHolders[it.id] = it
			it.reload()
			logger.debug("Registered {}", it.id)
		}
	}

	/**
	 * Retrieves a configuration holder associated with the given [id].
	 *
	 * @param id The resource location identifier for the configuration.
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
	 *
	 * @param clazz The class type of the configuration holder to retrieve.
	 */
	@JvmStatic
	@Suppress("UNCHECKED_CAST")
	fun <T : Any> get(clazz: Class<T>): ConfigHolder<T> =
		configHolders.values.first { clazz.arrayType().isInstance(it.config) } as ConfigHolder<T>
}