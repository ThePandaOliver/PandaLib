/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.api.utils.Environment
import dev.pandasystems.pandalib.core.platform.GameHelper
import net.minecraft.server.MinecraftServer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.nio.file.Path

@AutoService(GameHelper::class)
class GameHelperImpl : GameHelper {
	override val isDevelopment = !FMLLoader.isProduction()
	override val isProduction = FMLLoader.isProduction()

	override val environment = when (FMLLoader.getDist()) {
		Dist.CLIENT -> Environment.CLIENT
		Dist.DEDICATED_SERVER -> Environment.DEDICATED_SERVER
	}
	override val isClient = FMLLoader.getDist().isClient
	override val isDedicatedServer = FMLLoader.getDist().isDedicatedServer

	override val gameDir: Path = FMLPaths.GAMEDIR.get().toAbsolutePath().normalize()
	override val configDir: Path = FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize()
	override val modDir: Path = FMLPaths.MODSDIR.get().toAbsolutePath().normalize()

	override val server: MinecraftServer?
		get() = ServerLifecycleHooks.getCurrentServer()
}
