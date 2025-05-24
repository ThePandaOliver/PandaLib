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
package me.pandamods.pandalib.config.holders

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.architectury.platform.Platform
import me.pandamods.pandalib.config.Config
import me.pandamods.pandalib.config.ConfigData
import me.pandamods.pandalib.platform.Services
import me.pandamods.pandalib.utils.ClassUtils
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

open class ConfigHolder<T : ConfigData>(val configClass: Class<T>, val definition: Config) {
	val logger: Logger = LoggerFactory.getLogger(definition.modId + " | Config")
	val gson: Gson = newDefault.buildGson(GsonBuilder()).setPrettyPrinting().create()

	private val resourceLocation: ResourceLocation = ResourceLocation.fromNamespaceAndPath(definition.modId, definition.name)
	private val synchronize: Boolean = definition.synchronize
	private lateinit var config: T

	init {
		if (load()) {
			save()
		}
	}

	fun shouldSynchronize(): Boolean {
		return synchronize
	}

	val configPath: Path = Services.GAME.configDir.let {
		if (definition.directory.isBlank()) it else it.resolve(definition.directory)
	}.resolve(definition.name + ".json")

	open fun save() {
		val jsonObject = gson.toJsonTree(config).getAsJsonObject()
		this.config.onSave(this, jsonObject)
		
		val configPath = this.configPath
		try {
			Files.createDirectories(configPath.parent)
			val writer = Files.newBufferedWriter(configPath)
			this.gson.toJson(jsonObject, writer)
			writer.close()
			this.logger.info("Successfully saved config '{}'", definition.name)
		} catch (e: IOException) {
			this.logger.info("Failed to save config '{}'", definition.name)
			throw RuntimeException(e)
		}
	}

	open fun load(): Boolean {
		val configPath = this.configPath
		if (Files.exists(configPath)) {
			try {
				Files.newBufferedReader(configPath).use { reader ->
					val jsonObject = this.gson.fromJson<JsonObject>(reader, JsonObject::class.java)
					this.config = this.gson.fromJson(jsonObject, configClass)
					this.config.onLoad(this, jsonObject)
				}
			} catch (e: IOException) {
				this.logger.error("Failed to load config '{}', using default", definition.name, e)
				resetToDefault()
				return false
			}
		} else {
			resetToDefault()
			save()
		}
		this.logger.info("Successfully loaded config '{}'", definition.name)
		return true
	}

	fun resetToDefault() {
		this.config = this.newDefault
	}

	val newDefault: T
		/**
		 * @return Newly created class
		 */
		get() = ClassUtils.constructUnsafely(configClass)

	fun resourceLocation(): ResourceLocation {
		return resourceLocation
	}

	val langName: String
		get() = String.format("config.%s.%s", resourceLocation.namespace, resourceLocation.path)

	fun modID(): String {
		return this.definition.modId
	}

	/**
	 * @return Local config settings
	 */
	open fun get(): T? {
		return config
	}
}
