package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.api.config.ConfigSerializer
import dev.pandasystems.pandalib.core.logger
import dev.pandasystems.pandalib.impl.platform.Services
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import dev.pandasystems.pandalib.utils.replaceObjectInstanceUnsafely
import net.minecraft.resources.ResourceLocation
import java.io.File

class ConfigHolderImpl<T : Any>(
	val options: Configuration,
	override var config: T,
	val serializer: ConfigSerializer<T>
) : ConfigHolder<T> {
	val file: File = Services.GAME.configDir.resolve("${options.pathName}.json").toFile()

	override val id: ResourceLocation = resourceLocation(options.modId, options.pathName)

	private val configDefaultJson = serializer.serialize(config)

	override fun reload() {
		logger.debug("Reloading config {}...", id)
		try {
			if (file.exists()) {
				val json = file.readText()
				val newConfig = serializer.deserialize(json, config.javaClass)

				// If the config class is a singleton, then we replace its instance with the new config
				if (config::class.objectInstance != null)
					config::class.replaceObjectInstanceUnsafely(config)

				// Update the config with the new values after we are sure the instance was successfully replaced
				config = newConfig
				logger.debug("Config loaded {}", config)
			} else {
				logger.debug("Config could not be loaded {}", config)
				save()
			}
		} catch (e: Exception) {
			logger.error("Failed to reload config {}, Using default values as a fallback", id)
			logger.error("Error while loading config: {}", e.message, e)
		}
	}

	override fun save() {
		logger.debug("Saving config {}...", id)
		try {
			file.parentFile?.mkdirs()
			file.writeText(serializer.serialize(config))
			logger.debug("Config successfully saved to {}", file.absolutePath)
		} catch (e: Exception) {
			logger.error("Failed to save config {}", id, e)
		}
	}

	override fun resetToDefault() {
		logger.debug("Resetting config {} to default...", id)
		try {
			config = serializer.deserialize(configDefaultJson, config.javaClass)
			if (config::class.objectInstance != null)
				config::class.replaceObjectInstanceUnsafely(config)
			logger.debug("Config successfully reset to default (Not saved)")
		} catch (e: Exception) {
			logger.error("Failed to reset config {} to default", id, e)
		}
	}
}
