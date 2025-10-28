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

import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.pandalib.utils.event
import dev.pandasystems.pandalib.utils.gamePaths
import dev.pandasystems.pandalib.utils.objects
import dev.pandasystems.universalserializer.Serializer
import dev.pandasystems.universalserializer.elements.TreeObject
import dev.pandasystems.universalserializer.formats.JsonFormat
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.jvmName

class ConfigObject<T : Any>(
	val resourceLocation: ResourceLocation,
	private var configInstance: T,
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
		fun Any.serializeObject(): TreeObject {
			val treeElement = serializer.toTree(this, this::class.createType())
			if (!treeElement.isObject) return TreeObject()
			for ((klass, obj) in this::class.objects) {
				treeElement.asObject[klass.simpleName ?: klass.jvmName] = obj.serializeObject()
			}
			return treeElement.asObject
		}
		return configInstance.serializeObject()
	}

	private fun deserialize(treeObject: TreeObject) {
		fun Any.deserializeObject(treeObject: TreeObject, klass: KClass<*>): Any {
			serializer.fromTree(treeObject, klass)
			for ((klass, obj) in klass.objects) {
				val nextObject = treeObject.asObject[klass.simpleName ?: klass.jvmName]
				if (nextObject == null || !nextObject.isObject) continue
				obj.deserializeObject(nextObject.asObject, klass)
			}
			return this
		}
		@Suppress("UNCHECKED_CAST")
		configInstance = configInstance.deserializeObject(treeObject, configInstance::class) as T
	}

	override fun toString(): String {
		return resourceLocation.toString()
	}
}