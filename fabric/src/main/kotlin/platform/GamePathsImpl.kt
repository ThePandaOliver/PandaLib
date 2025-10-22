package dev.pandasystems.pandalib.fabric.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.GamePathsPlatform
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

@AutoService(GamePathsPlatform::class)
class GamePathsImpl : GamePathsPlatform {
	override val gameDir: Path = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()
	override val configDir: Path = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()
	override val modDir: Path = gameDir.resolve("mods")
}