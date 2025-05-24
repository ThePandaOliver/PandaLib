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

import dev.architectury.utils.Env
import me.pandamods.pandalib.platform.services.GameHelper
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Path

class GameHelperImpl : GameHelper {
	override val isDevelopmentEnvironment: Boolean
		get() = !FMLLoader.isProduction()

	override val isProductionEnvironment: Boolean
		get() = FMLLoader.isProduction()

	override val environment: Env
		get() = when (FMLLoader.getDist()) {
			Dist.CLIENT -> Env.CLIENT
			Dist.DEDICATED_SERVER -> Env.SERVER
		}

	override val isClient: Boolean
		get() = FMLLoader.getDist().isClient()

	override val isServer: Boolean
		get() = FMLLoader.getDist().isDedicatedServer()

	override val gameDir: Path
		get() = FMLPaths.GAMEDIR.get()

	override val configDir: Path
		get() = FMLPaths.CONFIGDIR.get()

	override val modDir: Path
		get() = FMLPaths.MODSDIR.get()
}
