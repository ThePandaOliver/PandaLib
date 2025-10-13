/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.platform.GameData
import dev.pandasystems.pandalib.fabric.PandaLibFabric
import dev.pandasystems.pandalib.utils.Environment
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.server.MinecraftServer
import java.nio.file.Path
import kotlin.collections.set
import kotlin.jvm.optionals.getOrNull

@AutoService(GameData::class)
class GameDataImpl : GameData {
	override val isDevelopment = FabricLoader.getInstance().isDevelopmentEnvironment
	override val isProduction = !FabricLoader.getInstance().isDevelopmentEnvironment

	override val environment = when (FabricLoader.getInstance().environmentType) {
		EnvType.CLIENT -> Environment.CLIENT
		EnvType.SERVER -> Environment.DEDICATED_SERVER
	}

	override val gameDir: Path = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()
	override val configDir: Path = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()
	override val modDir: Path = gameDir.resolve("mods")

	override val server: MinecraftServer?
		get() = PandaLibFabric.server

	private val modMap = mutableMapOf<String, GameData.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return FabricLoader.getInstance().isModLoaded(modId)
	}

	override fun getMod(modId: String): GameData.Mod? {
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
	) : GameData.Mod {
		override val id: String = metadata.id

		override val displayName: String = metadata.name

		override val description: String = metadata.description

		override val authors: List<String> = metadata.authors.map { it.name }

		override val version: String = metadata.version.friendlyString
	}
}
