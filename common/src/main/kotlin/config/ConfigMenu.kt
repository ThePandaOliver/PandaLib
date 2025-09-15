/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import net.minecraft.resources.ResourceLocation

open class ConfigMenu internal constructor(
	val name: ResourceLocation,
	val properties: List<ConfigProperty<*>>,
	val subMenus: List<ConfigMenu>
) {
	protected val configMap = mutableMapOf<String, Any?>()

	protected fun createMap(): Map<String, Any?> {
		configMap += properties.associate { it.createMapEntry().toPair() } + subMenus.associate { it.name.path.substringAfterLast("/") to it.createMap() }
		return configMap
	}
}