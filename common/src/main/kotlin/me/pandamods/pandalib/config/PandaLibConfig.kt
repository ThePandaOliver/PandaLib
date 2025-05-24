/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.config

import me.pandamods.pandalib.config.holders.ClientConfigHolder
import me.pandamods.pandalib.config.holders.CommonConfigHolder
import me.pandamods.pandalib.config.holders.ConfigHolder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.ResourceLocation
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

object PandaLibConfig {
	var configs: MutableMap<Class<out ConfigData>, ConfigHolder<out ConfigData>> = HashMap<Class<out ConfigData>, ConfigHolder<out ConfigData>>()

	fun <T : ConfigData, E : ConfigHolder<T>> register(configClass: Class<T>, provider: ConfigHolderProvider<T, E>): E {
		if (configs.containsKey(configClass)) throw RuntimeException(String.format("Config %s is already registered", configClass))

		val config = configClass.getAnnotation(Config::class.java)
		if (config == null) throw RuntimeException(String.format("%s is not annotated with @Config", configClass))

		val holder = provider.provide(configClass, config)
		configs.put(configClass, holder)
		return holder
	}

	fun <T : ConfigData> registerClient(configClass: Class<T>): ClientConfigHolder<T> {
		// Explicit type is required at compile time for some reason.
		return register<T, ClientConfigHolder<T>>(
			configClass
		) { configClass: Class<T>, config: Config -> ClientConfigHolder(configClass, config) }
	}

	fun <T : ConfigData> registerCommon(configClass: Class<T>): CommonConfigHolder<T> {
		// Explicit type is required at compile time for some reason.
		return register<T, CommonConfigHolder<T>>(
			configClass,
			ConfigHolderProvider { configClass: Class<T>, config: Config -> CommonConfigHolder(configClass, config) })
	}

	@JvmStatic
	fun getConfig(resourceLocation: ResourceLocation): Optional<ConfigHolder<out ConfigData>> {
		return configs.values.stream()
			.filter { configHolder: ConfigHolder<out ConfigData> -> configHolder.resourceLocation() == resourceLocation }.findFirst()
	}

	fun <T : ConfigData> getConfig(config: Class<T>): ConfigHolder<T> {
		@Suppress("UNCHECKED_CAST")
		return configs[config] as ConfigHolder<T>
	}

	fun getConfigs(modId: String): MutableMap<Class<*>, ConfigHolder<*>> {
		return configs.entries.stream()
			.filter { entry: MutableMap.MutableEntry<Class<out ConfigData>, ConfigHolder<out ConfigData>> -> entry.value.definition.modId == modId }
			.collect(Collectors.toMap(Function { it.key }, Function { it.value }))
	}

	/**
	 * Retrieves the configuration screen for the specified mod ID.
	 *
	 * @param modID the mod ID of the configuration
	 * @return the configuration screen
	 */
	@Deprecated(
		"""The config menu api will be added in the future, this is just here for quick support with future versions,
	  this method is deprecated and returns null."""
	)
	fun getConfigScreen(modID: String): Screen? {
		return null
	}

	/**
	 * Retrieves the configuration screen for the specified parent screen and mod ID.
	 *
	 * @param parent the parent screen
	 * @param modID  the mod ID of the configuration
	 * @return the configuration screen
	 */
	@Deprecated(
		"""The config menu api will be added in the future, this is just here for quick support with future versions,
	  this method is deprecated and returns null."""
	)
	fun getConfigScreen(parent: Screen, modID: String): Screen? {
		return null
	}

	/**
	 * Retrieves the configuration screen for the specified configClass.
	 *
	 * @param configClass the class of the config data
	 * @return the configuration screen
	 */
	@Deprecated(
		"""The config menu api will be added in the future, this is just here for quick support with future versions,
	  this method is deprecated and returns null."""
	)
	fun <T : ConfigData> getConfigScreen(configClass: Class<T>): Screen? {
		return null
	}

	/**
	 * Retrieves the configuration screen for the specified configClass.
	 *
	 * @param parent      the parent screen
	 * @param configClass the class of the config data
	 * @return the configuration screen
	 */
	@Deprecated(
		"""The config menu api will be added in the future, this is just here for quick support with future versions,
	  this method is deprecated and returns null."""
	)
	fun <T : ConfigData> getConfigScreen(parent: Screen, configClass: Class<T>): Screen? {
		return null
	}

	fun interface ConfigHolderProvider<T : ConfigData, E : ConfigHolder<T>> {
		fun provide(configClass: Class<T>, config: Config): E
	}
}
