package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.api.config.ConfigSerializer
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import dev.pandasystems.pandalib.impl.platform.Services
import net.minecraft.resources.ResourceLocation
import java.io.File
import kotlin.jvm.java

class ConfigHolderImpl<T : Any>(
	val options: Configuration,
	override var config: T,
	val serializer: ConfigSerializer<T>
) : ConfigHolder<T> {
	val file: File = Services.GAME.configDir.resolve("${options.pathName}.json").toFile()

	override val id: ResourceLocation = resourceLocation(options.modId, options.pathName)

	private val configDefaultJson = serializer.serialize(config)

	override fun reload() {
		if (file.exists()) {
			val json = file.readText()
			updateConfigWithJson(json)
		} else save()
	}

	override fun save() {
		if (!file.parentFile.exists())
			file.parentFile.mkdirs()
		file.writeText(serializer.serialize(config))
	}

	override fun resetToDefault() {
		updateConfigWithJson(configDefaultJson)
	}
	
	private fun updateConfigWithJson(json: String) {
		val source = serializer.deserialize(json)
		
		config::class.java.declaredFields.forEach { field ->
			field.isAccessible = true
			field.set(config, field.get(source))
		}
	}
}