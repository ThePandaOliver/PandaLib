package dev.pandasystems.pandalib.neoforge.platform

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.utils.Environment
import dev.pandasystems.pandalib.utils.GameEnvironmentPlatform
import net.minecraft.server.MinecraftServer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.server.ServerLifecycleHooks

@AutoService(GameEnvironmentPlatform::class)
class GameEnvironmentImpl : GameEnvironmentPlatform {
	override val isDevelopment: Boolean
		get() = !isProduction
	override val isProduction: Boolean
		get() = FMLEnvironment.isProduction()
	override val environment: Environment
		get() = when (FMLEnvironment.getDist()) {
			Dist.CLIENT -> Environment.CLIENT
			Dist.DEDICATED_SERVER -> Environment.DEDICATED_SERVER
		}
	override val server: MinecraftServer? get() = ServerLifecycleHooks.getCurrentServer()
}