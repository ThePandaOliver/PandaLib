package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.MinecraftRuntime
import dev.pandasystems.pandalib.core.MinecraftRuntimeEnvironment
import dev.pandasystems.pandalib.core.MinecraftRuntimeType
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment

class NeoForgeRuntime : MinecraftRuntime {
	override val type: MinecraftRuntimeType = MinecraftRuntimeType.NEO_FORGE
	override val environment: MinecraftRuntimeEnvironment = when (FMLEnvironment.getDist()) {
		Dist.CLIENT -> MinecraftRuntimeEnvironment.CLIENT
		Dist.DEDICATED_SERVER -> MinecraftRuntimeEnvironment.SERVER
	}
}