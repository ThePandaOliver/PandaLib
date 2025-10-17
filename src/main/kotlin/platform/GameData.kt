/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.platform

//? if fabric {
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata

//?} elif neoforge {
/*import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforgespi.language.IModInfo

*///?}

import dev.pandasystems.pandalib.utils.Environment
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

object GameData {
	val isDevelopment: Boolean
		get() = !isProduction
	val isProduction: Boolean
		get() {
			//? if fabric {
			return !FabricLoader.getInstance().isDevelopmentEnvironment
			//?} elif neoforge {
			/*return FMLEnvironment.isProduction()
			*///?}
		}

	val environment: Environment
		get() {
			//? if fabric {
			return when (FabricLoader.getInstance().environmentType) {
				EnvType.CLIENT -> Environment.CLIENT
				EnvType.SERVER -> Environment.DEDICATED_SERVER
			}
			//?} elif neoforge {
			/*return when (FMLEnvironment.getDist()) {
				Dist.CLIENT -> Environment.CLIENT
				Dist.DEDICATED_SERVER -> Environment.DEDICATED_SERVER
			}
			*///?}
		}
	val isClient get() = environment.isClient
	val isDedicatedServer get() = environment.isDedicatedServer

	val isHost: Boolean get() = server != null

	val gameDir: Path = let {
		//? if fabric {
		FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()
		//?} elif neoforge {
		/*FMLPaths.GAMEDIR.get().toAbsolutePath().normalize()
		*///?}
	}
	val configDir: Path = let {
		//? if fabric {
		FabricLoader.getInstance().configDir.toAbsolutePath().normalize()
		//?} elif neoforge {
		/*FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize()
		*///?}
	}
	val modDir: Path = let {
		//? if fabric {
		gameDir.resolve("mods")
		//?} elif neoforge {
		/*FMLPaths.MODSDIR.get().toAbsolutePath().normalize()
		*///?}
	}


	val client: Minecraft
		get() = Minecraft.getInstance()
	var server: MinecraftServer? = null
		internal set

	private val modMap = mutableMapOf<String, Mod>()

	fun isModLoaded(modId: String): Boolean {
		//? if fabric {
		return FabricLoader.getInstance().isModLoaded(modId)
		//?} elif neoforge {
		/*return ModList.get().isLoaded(modId)
		*///?}
	}

	fun getMod(modId: String): Mod? {
		//? if fabric {
		if (modMap.containsKey(modId)) return modMap[modId]
		return FabricLoader.getInstance().getModContainer(modId)
			.map { Mod(it) }.getOrNull()?.also {
				modMap[modId] = it
			}
		//?} elif neoforge {
		/*if (modMap.containsKey(modId)) return modMap[modId]
		return ModList.get().getModContainerById(modId)
			.map { Mod(it) }.getOrNull()?.also {
				modMap[modId] = it
			}
		*///?}
	}

	val mods: List<Mod> by lazy {
		//? if fabric {
		FabricLoader.getInstance().allMods.forEach { getMod(it.metadata.id) }
		modMap.values.toList()
		//?} elif neoforge {
		/*ModList.get().mods.forEach { getMod(it.modId) }
		modMap.values.toList()
		*///?}
	}
	val modIds: List<String> by lazy {
		//? if fabric {
		FabricLoader.getInstance().allMods.map { it.metadata.id }
		//?} elif neoforge {
		/*ModList.get().mods.map { it.modId }.toList()
		*///?}
	}

	class Mod(
		//? if fabric {
		val container: ModContainer,
		val metadata: ModMetadata = container.metadata
		//?} elif neoforge {
		/*val container: ModContainer,
		val info: IModInfo = container.modInfo
		*///?}
	) {
		val id: String = let {
			//? if fabric {
			metadata.id
			//?} elif neoforge {
			/*info.modId
			*///?}
		}

		val displayName: String = let {
			//? if fabric {
			metadata.name
			//?} elif neoforge {
			/*info.displayName
			*///?}
		}

		val description: String = let {
			//? if fabric {
			metadata.description
			//?} elif neoforge {
			/*info.description
			*///?}
		}

		val authors: List<String> = let {
			//? if fabric {
			metadata.authors.map { it.name }
			//?} elif neoforge {
			/*info.config.getConfigElement<Any>("authors")
				.map { it.toString() }.map { listOf(it) }.orElseGet { emptyList<String>() }
			*///?}
		}


		val version: String = let {
			//? if fabric {
			metadata.version.friendlyString
			//?} elif neoforge {
			/*info.version.toString()
			*///?}
		}
	}
}

@Deprecated("Use GameData instead", ReplaceWith("GameData"))
val game = GameData