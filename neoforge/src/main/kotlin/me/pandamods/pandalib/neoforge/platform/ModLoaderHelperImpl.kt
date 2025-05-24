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
import java.util.List
import java.util.function.Function

class ModLoaderHelperImpl : ModLoaderHelper {
	private val modMap: MutableMap<String, ModLoaderHelper.Mod> = HashMap<String, ModLoaderHelper.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return ModList.get().isLoaded(modId)
	}

	override fun getMod(modId: String): ModLoaderHelper.Mod {
		return modMap.computeIfAbsent(modId) { modId: String -> ModImpl(modId) }
	}

	override val mods: MutableList<ModLoaderHelper.Mod>
		get() {
			for (mod in ModList.get().getMods()) {
				getMod(mod.getModId())
			}

			return List.copyOf<ModLoaderHelper.Mod>(modMap.values)
		}

	override val modIds: MutableList<String>
		get() = ModList.get().getMods().stream().map<String> { obj: IModInfo -> obj!!.getModId() }.toList()

	private class ModImpl(modId: String) : ModLoaderHelper.Mod {
		private val container: ModContainer = ModList.get().getModContainerById(modId).orElseThrow()
		private val info: IModInfo = ModList.get().getMods().stream()
			.filter { modInfo: IModInfo -> modInfo!!.getModId() == modId }
			.findAny()
			.orElseThrow()

		override val id: String
			get() = info.getModId()

		override val displayName: String
			get() = info.getDisplayName()

		override val description: String
			get() = info.getDescription()

		override val authors: MutableList<String>
			get() {
				val optional = info.getConfig().getConfigElement<Any>("authors")
					.map<String>(Function { obj: Any -> java.lang.String.valueOf(obj) })
				return optional.map<MutableList<String>>(Function { e1: String -> List.of(e1) })
					.orElse(mutableListOf<String>())
			}

		override val version: String
			get() = info.getVersion().toString()
	}
}
