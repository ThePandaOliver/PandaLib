/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config.`object`

import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.serializer.ConfigSerializer
import dev.pandasystems.pandalib.config.serializer.JsonConfigSerializer
import dev.pandasystems.pandalib.listener.ListenerFactory
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.platform.game
import dev.pandasystems.pandalib.utils.constructClassUnsafely
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

open class ConfigObject<T: Any>(
	val resourceLocation: ResourceLocation,
	val configClass: Class<T>,
	getSerializer: (configClass: Class<T>) -> ConfigSerializer<T> = ::JsonConfigSerializer
) : Supplier<T> {
	private var instance = configClass.constructClassUnsafely()
	private val serializer: ConfigSerializer<T> = getSerializer(configClass)

	val path = game.configDir.toFile().resolve("${resourceLocation.namespace}/${resourceLocation.path}.${serializer.fileExtension}")

	val onSave = ListenerFactory.create<(configObject: ConfigObject<T>) -> Unit>()
	val onLoad = ListenerFactory.create<(configObject: ConfigObject<T>) -> Unit>()

	init {
		ConfigRegistry.register(this)
	}

	override fun get(): T {
		return instance
	}

	fun set(newInstance: T) {
		instance = newInstance
	}

	fun save() {
		if (!path.parentFile.exists())
			path.parentFile.mkdirs()
		val firstCreation = !path.exists()
		path.writeText(serializer.serialize(instance))
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
		set(serializer.deserialize(path.readText()))
		logger.info("Loaded config from $path")
		onLoad.invoker(this)
	}
}