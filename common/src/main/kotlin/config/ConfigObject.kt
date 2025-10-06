/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.pandasystems.pandalib.config.options.ConfigOption
import dev.pandasystems.pandalib.listener.ListenerFactory
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.platform.game
import dev.pandasystems.pandalib.utils.constructClassUnsafely
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

open class ConfigObject<T : Config>(
	val resourceLocation: ResourceLocation,
	val configClass: Class<T>
) : Supplier<T> {
	private var instance: T = configClass.constructClassUnsafely()
	private val gson = GsonBuilder().setPrettyPrinting().create()

	val path = game.configDir.toFile().resolve("${resourceLocation.namespace}/${resourceLocation.path}.json")

	val onSave = ListenerFactory.create<(configObject: ConfigObject<T>) -> Unit>()
	val onLoad = ListenerFactory.create<(configObject: ConfigObject<T>) -> Unit>()

	init {
		instance.initialize()
		ConfigRegistry.register(this)
	}

	override fun get(): T {
		return instance
	}

	fun save() {
		if (!path.parentFile.exists())
			path.parentFile.mkdirs()
		val firstCreation = !path.exists()
		path.writeText(gson.toJson(serialize()))
		if (firstCreation)
			logger.info("Created new config at $path")
		else
			logger.info("Saved config to $path")
		onSave.invoker(this)
	}

	fun load() {
		if (!path.exists()) {
			save()
			return
		}
		deserialize(gson.fromJson(path.readText(), JsonObject::class.java))
		logger.info("Loaded config from $path")
		onLoad.invoker(this)
	}

	private fun serialize(): JsonObject {
		fun Config.serialize(): JsonObject {
			val jsonObject = JsonObject()
			options.forEach { jsonObject.add(it.name, it.serialize()) }
			subCategories.forEach { (name, config) -> jsonObject.add(name, config.serialize()) }
			return jsonObject
		}

		return instance.serialize()
	}

	private fun deserialize(jsonObject: JsonObject) {
		fun Config.deserialize(jsonObject: JsonObject) {
			options.forEach { option -> jsonObject.get(option.name)?.let { option.deserialize(it) } }
			subCategories.forEach { (name, config) -> jsonObject.getAsJsonObject(name)?.let { config.deserialize(it) } }
		}
		return instance.deserialize(jsonObject)
	}
}