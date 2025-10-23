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

import dev.pandasystems.pandalib.config.options.ConfigOption
import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.pandalib.utils.event
import dev.pandasystems.pandalib.utils.gamePaths
import dev.pandasystems.universalserializer.Serializer
import dev.pandasystems.universalserializer.elements.TreeElement
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

	init {
		configInstance.lateInit(this)
	}

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
		configInstance.options.forEach { option ->
			var current: TreeElement = treeObject
			val pathSegments = option.pathName.split('.')
			pathSegments.forEachIndexed { index, segment ->
				if (index == pathSegments.lastIndex && current.isObject) {
					@Suppress("UNCHECKED_CAST")
					current.asObject[segment] = serializer.toTree(option.value, option.type)
				} else {
					current = current.asObject.computeIfAbsent(segment) { TreeObject() }
				}
			}
		}
		return treeObject
	}

	private fun deserialize(treeObject: TreeObject) {
		configInstance.options.forEach { option ->
			var current: TreeElement? = treeObject
			val pathSegments = option.pathName.split('.')
			pathSegments.forEach {
				if (current == null || !current.isObject)
					return@forEach
				current = current.asObject[it]
			}

			current?.let {
				val deserializedValue = serializer.fromTree(it, option.type)
				requireNotNull(deserializedValue) { "Failed to deserialize value for option ${option.pathName}" }
				@Suppress("UNCHECKED_CAST")
				(option as ConfigOption<Any>).value = deserializedValue
			}
		}
	}

	override fun toString(): String {
		return resourceLocation.toString()
	}
}