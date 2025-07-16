/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.registry

import dev.pandasystems.pandalib.core.platform.registryHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

object ReloadListenerRegistry {
	@JvmOverloads
	@JvmStatic
	fun register(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: MutableList<ResourceLocation> = mutableListOf()
	) {
		registryHelper.registerReloadListener(packType, listener, id, dependencies)
	}
}
