/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.platform

import dev.pandasystems.pandalib.utils.Environment
import dev.pandasystems.pandalib.utils.loadFirstService
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import java.nio.file.Path

interface GameData {
	val isDevelopment: Boolean
	val isProduction: Boolean

	val environment: Environment
	val isClient
		get() = environment.isClient
	val isDedicatedServer
		get() = environment.isDedicatedServer

	val isHost: Boolean
		get() = server != null

	val gameDir: Path
	val configDir: Path
	val modDir: Path

	val client: Minecraft
		get() = Minecraft.getInstance()
	val server: MinecraftServer?

	fun isModLoaded(modId: String): Boolean
	fun getMod(modId: String): Mod?
	val mods: List<Mod>
	val modIds: List<String>

	interface Mod {
		val id: String
		val displayName: String
		val description: String
		val authors: List<String>
		val version: String
	}
}

@JvmField
val game = loadFirstService<GameData>()
