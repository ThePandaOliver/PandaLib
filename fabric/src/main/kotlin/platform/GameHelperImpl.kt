package dev.pandasystems.pandalib.fabric.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.api.platform.GameHelper
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

class GameHelperImpl : GameHelper {
	override val isDevelopmentEnvironment: Boolean
		get() = FabricLoader.getInstance().isDevelopmentEnvironment

	override val isProductionEnvironment: Boolean
		get() = !FabricLoader.getInstance().isDevelopmentEnvironment

	override val environment: Env
		get() = when (FabricLoader.getInstance().environmentType) {
			EnvType.CLIENT -> Env.CLIENT
			EnvType.SERVER -> Env.SERVER
		}

	override val isClient: Boolean
		get() = FabricLoader.getInstance().environmentType == EnvType.CLIENT

	override val isServer: Boolean
		get() = FabricLoader.getInstance().environmentType == EnvType.SERVER

	override val gameDir: Path
		get() = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()

	override val configDir: Path
		get() = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()

	override val modDir: Path
		get() = gameDir.resolve("mods")
}
