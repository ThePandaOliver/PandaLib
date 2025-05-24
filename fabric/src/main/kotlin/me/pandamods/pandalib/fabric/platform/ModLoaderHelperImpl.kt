/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.fabric.platform

import me.pandamods.pandalib.platform.services.ModLoaderHelper
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
