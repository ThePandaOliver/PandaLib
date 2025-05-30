package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.core.utils.extensions.resourceLocation
import dev.pandasystems.pandalib.impl.platform.Services
import net.minecraft.resources.ResourceLocation
import java.io.File

class ConfigHolderImpl<T : Any>(
	val options: Configuration,
	override var config: T,
) : ConfigHolder<T> {
	val file: File = Services.GAME.configDir.resolve("${options.pathName}.json").toFile()

	override val id: ResourceLocation = resourceLocation(options.modId, options.pathName)

	private val configDefaultJson = ""

	override fun reload() {
		TODO("Not yet implemented")
	}

	override fun save() {
		TODO("Not yet implemented")
	}

	override fun resetToDefault() {
		TODO("Not yet implemented")
	}
}