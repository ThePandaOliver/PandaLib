package dev.pandasystems.pandalib.impl.config

import dev.pandasystems.pandalib.api.config.ConfigHolder
import dev.pandasystems.pandalib.api.config.ConfigSerializer
import dev.pandasystems.pandalib.impl.platform.Services
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.resources.ResourceLocation
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.jvm.java
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

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
			config = serializer.deserialize(json, config.javaClass)
			if (config::class.objectInstance != null)
				replaceObjectInstanceKotlin(config::class, config)
		} else save()
	}

	override fun save() {
		file.parentFile?.mkdirs()
		file.writeText(serializer.serialize(config))
	}

	override fun resetToDefault() {
		config = serializer.deserialize(configDefaultJson, config.javaClass)
		if (config::class.objectInstance != null)
			replaceObjectInstanceKotlin(config::class, config)
	}
}

fun replaceObjectInstanceKotlin(objectClass: KClass<*>, newInstance: Any) {
	try {
		val instanceProperty = objectClass.memberProperties
			.find { it.name == "INSTANCE" }

		instanceProperty?.let { prop ->
			prop.isAccessible = true
			val javaField = prop.javaField
			javaField?.let { field ->
				field.isAccessible = true

				// Remove final modifier (same as Java approach)
				val modifiersField = Field::class.java.getDeclaredField("modifiers")
				modifiersField.isAccessible = true
				modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

				field.set(null, newInstance)
			}
		}
	} catch (e: Exception) {
		e.printStackTrace()
	}
}
