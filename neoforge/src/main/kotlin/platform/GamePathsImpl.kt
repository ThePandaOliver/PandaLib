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

package dev.pandasystems.pandalib.neoforge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.GamePathsPlatform
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Path

@AutoService(GamePathsPlatform::class)
class GamePathsImpl : GamePathsPlatform {
	override val gameDir: Path = FMLPaths.GAMEDIR.get().toAbsolutePath().normalize()
	override val configDir: Path = FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize()
	override val modDir: Path = FMLPaths.MODSDIR.get().toAbsolutePath().normalize()
}