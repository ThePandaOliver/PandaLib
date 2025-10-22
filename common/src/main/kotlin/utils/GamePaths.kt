package dev.pandasystems.pandalib.utils

import java.nio.file.Path

val gamePaths = loadFirstService<GamePathsPlatform>()

interface GamePathsPlatform {
	val gameDir: Path
	val configDir: Path
	val modDir: Path
}