/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.config

import net.minecraft.resources.ResourceLocation

interface ConfigHolder<T : Any> {
	val configClass: Class<T>
	var config: T
	val id: ResourceLocation

	fun reload()
	fun save()
	fun resetToDefault()
}