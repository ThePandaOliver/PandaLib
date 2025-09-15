/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import net.minecraft.resources.ResourceLocation

class Configuration internal constructor(
	name: ResourceLocation,
	properties: List<ConfigProperty<*>>,
	subMenus: List<ConfigMenu>
) : ConfigMenu(name, properties, subMenus) {
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

	}

	fun save() {

	}

	fun resetToDefault() {
		fun resetMenu(menu: ConfigMenu) {
			menu.properties.forEach { it.resetToDefault() }
			menu.subMenus.forEach { resetMenu(it) }
		}

		resetMenu(this)
	}
}