package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.api.config.ConfigSerializer
import dev.pandasystems.pandalib.core.logger
import dev.pandasystems.pandalib.impl.platform.Services
import dev.pandasystems.pandalib.utils.constructClassUnsafely
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.resources.ResourceLocation
import java.io.File

class ConfigHolderImpl<T : Any>(
	val options: Configuration,
	override val configClass: Class<T>,
	val serializer: ConfigSerializer<T>
) : ConfigHolder<T> {
	override val id: ResourceLocation = resourceLocation(options.modId, options.pathName)
	override lateinit var config: T
	
	val file: File = Services.GAME.configDir.resolve("${options.pathName}.json").toFile()

	override fun reload() {
		logger.debug("Reloading config {}...", id)
		try {
			if (file.exists()) {
				val json = file.readText()
				config = serializer.deserialize(json, config.javaClass)
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
			config = constructClassUnsafely(configClass)
			logger.debug("Config successfully reset to default (Not saved)")
		} catch (e: Exception) {
			logger.error("Failed to reset config {} to default", id, e)
		}
	}
}
