package dev.pandasystems.pandalib.neoforge.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.api.platform.GameHelper
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Path

class GameHelperImpl : GameHelper {
	override val isDevelopmentEnvironment = !FMLLoader.isProduction()

	override val isProductionEnvironment = FMLLoader.isProduction()

	override val environment = when (FMLLoader.getDist()) {
		Dist.CLIENT -> Env.CLIENT
		Dist.DEDICATED_SERVER -> Env.SERVER
	}

	override val isClient = FMLLoader.getDist().isClient

	override val isServer = FMLLoader.getDist().isDedicatedServer

	override val gameDir: Path = FMLPaths.GAMEDIR.get()

	override val configDir: Path = FMLPaths.CONFIGDIR.get()

	override val modDir: Path = FMLPaths.MODSDIR.get()
}
