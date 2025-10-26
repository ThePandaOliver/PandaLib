/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.pandalib.utils.event
import dev.pandasystems.pandalib.utils.gamePaths
import dev.pandasystems.universalserializer.Serializer
import dev.pandasystems.universalserializer.elements.TreeObject
import dev.pandasystems.universalserializer.formats.JsonFormat
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class ConfigObject<T : Config>(
	val resourceLocation: ResourceLocation,
	private val configInstance: T,
	val serializer: Serializer = Serializer(JsonFormat(prettyPrint = true))
) : Supplier<T> {
	val filePath = gamePaths.configDir.toFile().resolve("${resourceLocation.namespace}/${resourceLocation.path}.json")

	val onSave = event<(configObject: ConfigObject<T>) -> Unit>()
	val onLoad = event<(configObject: ConfigObject<T>) -> Unit>()

	override fun get(): T {
		return configInstance
	}

	fun save() {
		if (!filePath.parentFile.exists())
			filePath.parentFile.mkdirs()
		val firstCreation = !filePath.exists()
		filePath.writeText(serializer.toValue(serialize()))
		if (firstCreation)
			pandalibLogger.info("Created new config at $filePath")
		else
			pandalibLogger.info("Saved config to $filePath")
		pandalibLogger.debug(" - config data ${serializer.toValue(serialize())}")
		onSave.invoker(this)
	}

	fun load() {
		if (!filePath.exists()) {
			save()
			return
		}
		val tree = serializer.fromValue<TreeObject>(filePath.readText())
		requireNotNull(tree) { "Failed to load config from $filePath" }
		deserialize(tree)
		pandalibLogger.info("Loaded config from $filePath")
		pandalibLogger.debug(" - config data ${serializer.toValue(tree)}")
		onLoad.invoker(this)
	}

	private fun serialize(): TreeObject {
		val treeObject = TreeObject()
		configInstance.allOptions.forEach { option ->
			var current = treeObject
			if (option.parentPath.isNotEmpty()) {
				option.parentPath.split(".").forEach { segment ->
					check(current.isObject) { "Cannot create object segment '$segment' under non-object parent" }
					current = current.asObject.computeIfAbsent(segment) { TreeObject() }.asObject
				}
			}
			check(!current.contains(option.name)) { "Option '${option.name}' already exists in config tree" }
			current[option.name] = serializer.toTree(option.value, option.valueType)
		}
		return treeObject
	}

	private fun deserialize(treeObject: TreeObject) {
		configInstance.allOptions.forEach { option ->
			var current = treeObject
			if (option.parentPath.isNotEmpty()) {
				for (segment in option.parentPath.split(".")) {
					val currentElement = current[segment] ?: break
					check(currentElement.isObject) { "Cannot deserialize object segment '$segment' under non-object parent" }
					current = currentElement.asObject
				}
			}
			current[option.name]?.let {
				val deserializedValue = serializer.fromTree(it, option.valueType)
				requireNotNull(deserializedValue) { "Failed to deserialize value for option ${option.name}" }
				@Suppress("UNCHECKED_CAST")
				(option as? Config.Option<Any>)?.value = deserializedValue
			}
		}
	}

	override fun toString(): String {
		return resourceLocation.toString()
	}
}