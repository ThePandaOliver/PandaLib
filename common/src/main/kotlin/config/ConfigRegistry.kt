/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.utils.constructClassUnsafely
import net.minecraft.resources.ResourceLocation

object ConfigRegistry {
	private val configObjects: MutableMap<ResourceLocation, ConfigObject<*>> = mutableMapOf()

	fun <T : Config> register(configObject: ConfigObject<T>) {
		val key = configObject.resourceLocation
		if (configObjects.containsKey(key)) {
			throw IllegalArgumentException("ConfigObject with resource location $key is already registered.")
		}
		configObjects[key] = configObject
	}


	fun <T : Config> create(resourceLocation: ResourceLocation, configInstance: T): ConfigObject<T> {
		val configObject = ConfigObject(resourceLocation, configInstance)
		val key = configObject.resourceLocation
		if (configObjects.containsKey(key)) {
			throw IllegalArgumentException("ConfigObject with resource location $key is already registered.")
		}
		configObjects[key] = configObject
		return configObject
	}

	fun <T : Config> create(resourceLocation: ResourceLocation, configConstructor: () -> T): ConfigObject<T> = create(resourceLocation, configConstructor())
	fun <T : Config> create(resourceLocation: ResourceLocation, configClass: Class<T>): ConfigObject<T> = create(resourceLocation, configClass.constructClassUnsafely())

	@Suppress("UNCHECKED_CAST")
	fun <T : Config> get(resourceLocation: ResourceLocation): ConfigObject<T>? {
		return configObjects[resourceLocation] as? ConfigObject<T>
	}
}