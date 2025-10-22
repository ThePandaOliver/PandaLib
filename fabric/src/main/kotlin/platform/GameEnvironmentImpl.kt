package dev.pandasystems.pandalib.fabric.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.Environment
import dev.pandasystems.pandalib.utils.GameEnvironmentPlatform
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer

@AutoService(GameEnvironmentPlatform::class)
class GameEnvironmentImpl : GameEnvironmentPlatform {
	override val isDevelopment: Boolean
		get() = FabricLoader.getInstance().isDevelopmentEnvironment
	override val isProduction: Boolean
		get() = !isDevelopment
	override val environment: Environment
		get() = when (FabricLoader.getInstance().environmentType) {
			EnvType.CLIENT -> Environment.CLIENT
			EnvType.SERVER -> Environment.DEDICATED_SERVER
		}
	override var server: MinecraftServer? = null
}