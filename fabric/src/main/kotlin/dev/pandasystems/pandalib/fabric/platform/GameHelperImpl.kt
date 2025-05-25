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
package dev.pandasystems.pandalib.fabric.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.platform.services.GameHelper
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

class GameHelperImpl : GameHelper {
	override val isDevelopmentEnvironment: Boolean
		get() = FabricLoader.getInstance().isDevelopmentEnvironment

	override val isProductionEnvironment: Boolean
		get() = !FabricLoader.getInstance().isDevelopmentEnvironment

	override val environment: Env
		get() = when (FabricLoader.getInstance().environmentType) {
			EnvType.CLIENT -> Env.CLIENT
			EnvType.SERVER -> Env.SERVER
		}

	override val isClient: Boolean
		get() = FabricLoader.getInstance().environmentType == EnvType.CLIENT

	override val isServer: Boolean
		get() = FabricLoader.getInstance().environmentType == EnvType.SERVER

	override val gameDir: Path
		get() = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()

	override val configDir: Path
		get() = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()

	override val modDir: Path
		get() = gameDir.resolve("mods")
}
