package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.MinecraftRuntime
import dev.pandasystems.pandalib.core.MinecraftRuntimeEnvironment
import dev.pandasystems.pandalib.core.MinecraftRuntimeType
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader

class FabricRuntime : MinecraftRuntime {
	override val type: MinecraftRuntimeType = MinecraftRuntimeType.FABRIC
	override val environment: MinecraftRuntimeEnvironment = when (FabricLoader.getInstance().environmentType) {
		EnvType.CLIENT -> MinecraftRuntimeEnvironment.CLIENT
		EnvType.SERVER -> MinecraftRuntimeEnvironment.SERVER
	}
}