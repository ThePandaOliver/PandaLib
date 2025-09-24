/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.`object`.ConfigObject
import net.minecraft.resources.ResourceLocation

object ConfigRegistry {
	private val configObjects: MutableMap<ResourceLocation, ConfigObject<*>> = mutableMapOf()

	fun <T: Any> register(configObject: ConfigObject<T>) {
		val key = configObject.resourceLocation
		if (configObjects.containsKey(key)) {
			throw IllegalArgumentException("ConfigObject with resource location $key is already registered.")
		}
		configObjects[key] = configObject
	}

	@Suppress("UNCHECKED_CAST")
	fun <T: Any> get(resourceLocation: ResourceLocation): ConfigObject<T>? {
		return configObjects[resourceLocation] as? ConfigObject<T>
	}
}