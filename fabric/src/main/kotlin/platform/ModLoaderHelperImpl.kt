/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform

import dev.pandasystems.pandalib.core.platform.ModLoaderHelper
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata
import kotlin.jvm.optionals.getOrNull

class ModLoaderHelperImpl : ModLoaderHelper {
	private val modMap = mutableMapOf<String, ModLoaderHelper.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return FabricLoader.getInstance().isModLoaded(modId)
	}

	override fun getMod(modId: String): ModLoaderHelper.Mod? {
		if (modMap.containsKey(modId)) return modMap[modId]
		return FabricLoader.getInstance().getModContainer(modId)
			.map { ModImpl(it) }.getOrNull()?.also {
				modMap[modId] = it
			}
	}

	override val mods by lazy {
		FabricLoader.getInstance().allMods.forEach { getMod(it.metadata.id) }
		modMap.values.toList()
	}

	override val modIds by lazy { FabricLoader.getInstance().allMods.map { it.metadata.id } }

	private class ModImpl(
		val container: ModContainer,
		val metadata: ModMetadata = container.metadata
	) : ModLoaderHelper.Mod {
		override val id: String = metadata.id

		override val displayName: String = metadata.name

		override val description: String = metadata.description

		override val authors: List<String> = metadata.authors.map { it.name }

		override val version: String = metadata.version.friendlyString
	}
}
