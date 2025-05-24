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
package me.pandamods.pandalib.neoforge.platform

import me.pandamods.pandalib.platform.services.ModLoaderHelper
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.neoforgespi.language.IModInfo
import kotlin.jvm.optionals.getOrNull

class ModLoaderHelperImpl : ModLoaderHelper {
	private val modMap = mutableMapOf<String, ModLoaderHelper.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return ModList.get().isLoaded(modId)
	}

	override fun getMod(modId: String): ModLoaderHelper.Mod? {
		if (modMap.containsKey(modId)) return modMap[modId]
		return ModList.get().getModContainerById(modId)
			.map { ModImpl(it) }.getOrNull()?.also {
				modMap[modId] = it
			}
	}

	override val mods by lazy {
		ModList.get().mods.forEach { getMod(it.modId) }
		modMap.values.toList()
	}

	override val modIds by lazy { ModList.get().mods.map { it.modId }.toList() }

	private class ModImpl(
		val container: ModContainer,
		val info: IModInfo = container.modInfo
	) : ModLoaderHelper.Mod {
		override val id: String = info.modId

		override val displayName: String = info.displayName

		override val description: String = info.description

		override val authors: List<String> = info.config.getConfigElement<Any>("authors")
			.map { it.toString() }.map { listOf(it) }.orElseGet { emptyList<String>() }

		override val version: String = info.version.toString()
	}
}
