package dev.pandasystems.pandalib.fabric

import dev.pandasystems.pandalib.core.IMinecraftRuntime
import dev.pandasystems.pandalib.core.RuntimeEnvironment
import dev.pandasystems.pandalib.core.RuntimeType
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader

object FabricRuntime : IMinecraftRuntime {
	override val type: RuntimeType = RuntimeType.FABRIC
	override val environment: RuntimeEnvironment = when (FabricLoader.getInstance().environmentType) {
		EnvType.CLIENT -> RuntimeEnvironment.CLIENT
		EnvType.SERVER -> RuntimeEnvironment.SERVER
	}
}