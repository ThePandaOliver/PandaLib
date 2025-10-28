/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config

import net.minecraft.resources.ResourceLocation

object ConfigRegistry {
	private val configObjects: MutableMap<ResourceLocation, ConfigObject<*>> = mutableMapOf()

	fun <T : Any> register(configObject: ConfigObject<T>) {
		val key = configObject.resourceLocation
		if (configObjects.containsKey(key)) {
			throw IllegalArgumentException("ConfigObject with resource location $key is already registered.")
		}
		configObjects[key] = configObject
	}


	fun <T : Any> create(resourceLocation: ResourceLocation, configInstance: T): ConfigObject<T> {
		val configObject = ConfigObject(resourceLocation, configInstance)
		val key = configObject.resourceLocation
		if (configObjects.containsKey(key)) {
			throw IllegalArgumentException("ConfigObject with resource location $key is already registered.")
		}
		configObjects[key] = configObject
		return configObject
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Any> get(resourceLocation: ResourceLocation): ConfigObject<T>? {
		return configObjects[resourceLocation] as? ConfigObject<T>
	}
}