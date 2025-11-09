package dev.pandasystems.pandalib.forge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.GamePathsPlatform
import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

@AutoService(GamePathsPlatform::class)
class GamePathsImpl : GamePathsPlatform {
	override val gameDir: Path = FMLPaths.GAMEDIR.get().toAbsolutePath().normalize()
	override val configDir: Path = FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize()
	override val modDir: Path = FMLPaths.MODSDIR.get().toAbsolutePath().normalize()
}