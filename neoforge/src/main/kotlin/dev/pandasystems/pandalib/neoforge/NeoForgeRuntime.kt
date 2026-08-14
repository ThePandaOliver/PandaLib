package dev.pandasystems.pandalib.neoforge

import dev.pandasystems.pandalib.core.IMinecraftRuntime
import dev.pandasystems.pandalib.core.RuntimeEnvironment
import dev.pandasystems.pandalib.core.RuntimeType
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment

object NeoForgeRuntime : IMinecraftRuntime {
	override val type: RuntimeType = RuntimeType.NEO_FORGE
	override val environment: RuntimeEnvironment = when (FMLEnvironment.getDist()) {
		Dist.CLIENT -> RuntimeEnvironment.CLIENT
		Dist.DEDICATED_SERVER -> RuntimeEnvironment.SERVER
	}
}