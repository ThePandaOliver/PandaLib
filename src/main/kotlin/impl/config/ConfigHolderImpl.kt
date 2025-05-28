package dev.pandasystems.pandalib.impl.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.core.utils.setFieldUnsafely
import dev.pandasystems.pandalib.impl.platform.Services
import java.io.File

class ConfigHolderImpl<T : Any>(
	val options: Configuration,
	override var config: T
) : ConfigHolder<T> {
	val gson: Gson = GsonBuilder().setPrettyPrinting().create()
	val file: File = Services.GAME.configDir.resolve(options.pathName).toFile()

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