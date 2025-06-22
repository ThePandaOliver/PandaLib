/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.api.platform.GameHelper
import net.minecraft.server.MinecraftServer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.nio.file.Path

class GameHelperImpl : GameHelper {
	override val isDevelopmentEnvironment = !FMLLoader.isProduction()

	override val isProductionEnvironment = FMLLoader.isProduction()

	override val environment = when (FMLLoader.getDist()) {
		Dist.CLIENT -> Env.CLIENT
		Dist.DEDICATED_SERVER -> Env.SERVER
	}

	override val isClient = FMLLoader.getDist().isClient

	override val isServer = FMLLoader.getDist().isDedicatedServer

	override val gameDir: Path = FMLPaths.GAMEDIR.get()

	override val configDir: Path = FMLPaths.CONFIGDIR.get()

	override val modDir: Path = FMLPaths.MODSDIR.get()

	override val server: MinecraftServer?
		get() = ServerLifecycleHooks.getCurrentServer()
}
