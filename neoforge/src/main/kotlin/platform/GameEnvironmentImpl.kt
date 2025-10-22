/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.neoforge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.Environment
import dev.pandasystems.pandalib.utils.GameEnvironmentPlatform
import net.minecraft.server.MinecraftServer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforge.server.ServerLifecycleHooks

@AutoService(GameEnvironmentPlatform::class)
class GameEnvironmentImpl : GameEnvironmentPlatform {
	override val isDevelopment: Boolean
		get() = !isProduction
	override val isProduction: Boolean
		get() = FMLLoader.isProduction()
	override val environment: Environment
		get() = when (FMLLoader.getDist()) {
			Dist.CLIENT -> Environment.CLIENT
			Dist.DEDICATED_SERVER -> Environment.DEDICATED_SERVER
		}
	override val server: MinecraftServer? get() = ServerLifecycleHooks.getCurrentServer()
}