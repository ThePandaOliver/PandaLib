/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import net.minecraft.resources.ResourceLocation

class ConfigBuilder internal constructor(val name: String) {
	val properties = mutableListOf<ConfigProperty<*>>()
	val subMenus = mutableListOf<ConfigBuilder>()

	fun addProperty(property: ConfigProperty<*>) {
		properties.add(property)
	}

	fun createSubMenu(name: String, block: ConfigBuilder.() -> Unit) {
		subMenus.add(ConfigBuilder(name).apply(block))
	}
}

fun createConfigBuilder(name: ResourceLocation, block: ConfigBuilder.() -> Unit): Configuration {
	val builder = ConfigBuilder(name.path).apply(block)

	fun createMenu(menuBuilder: ConfigBuilder, path: ResourceLocation): ConfigMenu {
		return ConfigMenu(path, menuBuilder.properties, menuBuilder.subMenus.map { createMenu(it, name.withPrefix("/${it.name}")) })
	}

	return Configuration(name, builder.properties, builder.subMenus.map { createMenu(it, name.withPrefix("/${it.name}")) })
}