/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.platform.GameHelper
import dev.pandasystems.pandalib.fabric.PandaLibFabric
import dev.pandasystems.pandalib.api.utils.Environment
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import java.nio.file.Path

@AutoService(GameHelper::class)
class GameHelperImpl : GameHelper {
	override val isDevelopment = FabricLoader.getInstance().isDevelopmentEnvironment
	override val isProduction = !FabricLoader.getInstance().isDevelopmentEnvironment

	override val environment = when (FabricLoader.getInstance().environmentType) {
		EnvType.CLIENT -> Environment.CLIENT
		EnvType.SERVER -> Environment.DEDICATED_SERVER
	}
	override val isClient = FabricLoader.getInstance().environmentType == EnvType.CLIENT
	override val isDedicatedServer = FabricLoader.getInstance().environmentType == EnvType.SERVER

	override val gameDir: Path = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()
	override val configDir: Path = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()
	override val modDir: Path = gameDir.resolve("mods")

	override val server: MinecraftServer?
		get() = PandaLibFabric.server
}
