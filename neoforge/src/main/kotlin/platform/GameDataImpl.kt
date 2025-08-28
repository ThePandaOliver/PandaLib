/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.Environment
import dev.pandasystems.pandalib.platform.GameData
import net.minecraft.server.MinecraftServer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.server.ServerLifecycleHooks
import net.neoforged.neoforgespi.language.IModInfo
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

@AutoService(GameData::class)
class GameDataImpl : GameData {
	override val isDevelopment = !FMLLoader.isProduction()
	override val isProduction = FMLLoader.isProduction()

	override val environment = when (FMLLoader.getDist()) {
		Dist.CLIENT -> Environment.CLIENT
		Dist.DEDICATED_SERVER -> Environment.DEDICATED_SERVER
	}

	override val gameDir: Path = FMLPaths.GAMEDIR.get().toAbsolutePath().normalize()
	override val configDir: Path = FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize()
	override val modDir: Path = FMLPaths.MODSDIR.get().toAbsolutePath().normalize()

	override val server: MinecraftServer?
		get() = ServerLifecycleHooks.getCurrentServer()

	private val modMap = mutableMapOf<String, GameData.Mod>()

	override fun isModLoaded(modId: String): Boolean {
		return ModList.get().isLoaded(modId)
	}

	override fun getMod(modId: String): GameData.Mod? {
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
	) : GameData.Mod {
		override val id: String = info.modId

		override val displayName: String = info.displayName

		override val description: String = info.description

		override val authors: List<String> = info.config.getConfigElement<Any>("authors")
			.map { it.toString() }.map { listOf(it) }.orElseGet { emptyList<String>() }

		override val version: String = info.version.toString()
	}
}
