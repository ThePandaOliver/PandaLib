/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
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

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.Environment
import dev.pandasystems.pandalib.utils.GameEnvironmentPlatform
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer

@AutoService(GameEnvironmentPlatform::class)
class GameEnvironmentImpl : GameEnvironmentPlatform {
	override val isDevelopment: Boolean
		get() = FabricLoader.getInstance().isDevelopmentEnvironment
	override val isProduction: Boolean
		get() = !isDevelopment
	override val environment: Environment
		get() = when (FabricLoader.getInstance().environmentType) {
			EnvType.CLIENT -> Environment.CLIENT
			EnvType.SERVER -> Environment.DEDICATED_SERVER
		}
	override var server: MinecraftServer? = null

	override val isFabric: Boolean
		get() = true
}