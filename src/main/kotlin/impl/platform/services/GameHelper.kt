package dev.pandasystems.pandalib.impl.platform.services

import dev.architectury.utils.Env
import java.nio.file.Path

interface GameHelper {
	val isDevelopmentEnvironment: Boolean
	val isProductionEnvironment: Boolean

	val environment: Env
	val isClient: Boolean
	val isServer: Boolean

	val gameDir: Path
	val configDir: Path
	val modDir: Path
}
