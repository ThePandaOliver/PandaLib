/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import dev.pandasystems.pandalib.config.options.ConfigOption
import dev.pandasystems.pandalib.config.serializer.ConfigSerialization
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.platform.game
import dev.pandasystems.pandalib.utils.event
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

open class ConfigObject<T : Config>(
	val resourceLocation: ResourceLocation,
	private val configInstance: T
) : Supplier<T> {
	private val gson = GsonBuilder().setPrettyPrinting().create()

	val filePath = game.configDir.toFile().resolve("${resourceLocation.namespace}/${resourceLocation.path}.json")

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
		filePath.writeText(gson.toJson(serialize()))
		if (firstCreation)
			logger.info("Created new config at $filePath")
		else
			logger.info("Saved config to $filePath")
		onSave.invoker(this)
	}

	fun load() {
		if (!filePath.exists()) {
			save()
			return
		}
		deserialize(gson.fromJson(filePath.readText(), JsonObject::class.java))
		logger.info("Loaded config from $filePath")
		onLoad.invoker(this)
	}

	private fun serialize(): JsonObject = ConfigSerialization.serialize(configInstance).asJsonObject

	private fun deserialize(jsonObject: JsonObject) {
		configInstance.options.forEach { option ->
			var current: JsonElement? = jsonObject
			val pathSegments = option.pathName.split('.')
			pathSegments.forEach {
				if (current == null || !current.isJsonObject)
					return@forEach
				current = current.asJsonObject.get(it)
			}

			current?.let {
				@Suppress("UNCHECKED_CAST")
				(option as ConfigOption<Any>).value =
					requireNotNull(ConfigSerialization.deserialize(it, option.type)) { "Failed to deserialize value for option ${option.pathName}" }
			}
		}
	}

	override fun toString(): String {
		return resourceLocation.toString()
	}
}