/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.serializer.ConfigSerializer
import dev.pandasystems.pandalib.config.serializer.JsonConfigSerializer
import dev.pandasystems.pandalib.platform.game
import net.minecraft.resources.ResourceLocation
import java.io.File

class Configuration internal constructor(
	name: ResourceLocation,
	properties: List<ConfigProperty<*>>,
	subMenus: List<ConfigMenu>
) : ConfigMenu(name, properties, subMenus) {
	val configFile: File = game.configDir.resolve("${name.namespace}/${name.path}.json").toFile()
	private val serializer: ConfigSerializer = JsonConfigSerializer()

	init {
		fun initializeMenu(menu: ConfigMenu) {
			menu.properties.forEach {
				it.configParent = this
				it.menuParent = menu
			}
			menu.subMenus.forEach { initializeMenu(it) }
		}
		initializeMenu(this)

		createMap()
	}

	fun load() {
		if (!configFile.exists()) {
			save()
			return
		} else {
			val text = configFile.readText()
			val loadedMap = createMap()
			serializer.deserialize(text, loadedMap)

			fun applyMap(menu: ConfigMenu, map: Map<String, ConfigStructElement>) {
				menu.properties.forEach { property ->
					property as ConfigProperty<Any?>
					property.value = (map[property.name]?.let { it.property?.value } ?: property.default)
				}
				menu.subMenus.forEach {
					val element = map[it.name.path.substringAfterLast("/")]
					if (element != null && element.menu != null) {
						applyMap(it, element.menu)
					}
				}
			}

			applyMap(this, loadedMap)
		}
	}

	fun save() {
		configFile.parentFile?.mkdirs()
		configFile.writeText(serializer.serialize(configMap))
		println("Saved config to ${configFile.absolutePath}")
	}

	fun resetToDefault() {
		fun resetMenu(menu: ConfigMenu) {
			menu.properties.forEach { it.resetToDefault() }
			menu.subMenus.forEach { resetMenu(it) }
		}

		resetMenu(this)
	}

	operator fun get(name: String): ConfigProperty<> {
		return subMenus.firstOrNull { it.name.path.substringAfterLast("/") == name }
	}
}