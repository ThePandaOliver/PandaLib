/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.core.platform

import dev.pandasystems.pandalib.api.utils.Environment
import net.minecraft.server.MinecraftServer
import java.nio.file.Path

interface GameHelper {
	val isDevelopment: Boolean
	val isProduction: Boolean

	val environment: Environment
	val isClient get() = environment.isClient
	val isDedicatedServer get() = environment.isDedicatedServer

	val gameDir: Path
	val configDir: Path
	val modDir: Path

	@Deprecated("Temporary method for getting the server instance. Will be removed in the future.", level = DeprecationLevel.WARNING)
	val server: MinecraftServer?
}
