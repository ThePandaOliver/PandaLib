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

package dev.pandasystems.pandalib.mc1_21_11.fabric.game

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.game.MinecraftGame
import dev.pandasystems.pandalib.utils.Version

@AutoService(MinecraftGame::class)
class MinecraftGameImpl : MinecraftGame() {
	override val compatibleVersion: Version
		get() = Version.mc1_21_11
}