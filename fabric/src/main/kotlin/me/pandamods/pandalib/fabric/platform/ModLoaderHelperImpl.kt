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
import net.fabricmc.loader.api.metadata.Person
import java.util.List

class ModLoaderHelperImpl : ModLoaderHelper {
	val modMap: MutableMap<String, ModLoaderHelper.Mod> = HashMap<String, ModLoaderHelper.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return FabricLoader.getInstance().isModLoaded(modId)
	}

	override fun getMod(modId: String): ModLoaderHelper.Mod {
		return modMap.computeIfAbsent(modId) { modId: String -> ModImpl(modId) }
	}

	override val mods: MutableList<ModLoaderHelper.Mod>
		get() {
			for (mod in FabricLoader.getInstance().getAllMods()) {
				getMod(mod.getMetadata().getId())
			}

			return List.copyOf<ModLoaderHelper.Mod>(modMap.values)
		}

	override val modIds: MutableList<String>
		get() = FabricLoader.getInstance().getAllMods().stream()
			.map<ModMetadata> { obj: ModContainer -> obj!!.getMetadata() }.map<String> { obj: ModMetadata -> obj!!.getId() }
			.toList()

	private class ModImpl(modId: String) : ModLoaderHelper.Mod {
		private val container: ModContainer = FabricLoader.getInstance().getModContainer(modId).orElseThrow()
		private val metadata: ModMetadata = this.container.getMetadata()

		override val id: String
			get() = metadata.getId()

		override val displayName: String
			get() = metadata.getName()

		override val description: String
			get() = metadata.getDescription()

		override val authors: MutableList<String>
			get() = metadata.getAuthors().stream().map<String> { obj: Person -> obj!!.getName() }.toList()

		override val version: String
			get() = metadata.getVersion().getFriendlyString()
	}
}
