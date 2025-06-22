package dev.pandasystems.pandalib.impl.registry

import dev.pandasystems.pandalib.api.platform.registryHelper
import net.minecraft.core.Registry

@Suppress("unused")
object RegistryRegister {
	@JvmStatic
	fun <T> register(registry: Registry<T>): Registry<T> {
		registryHelper.registerNewRegistry<T>(registry)
		return registry
	}
}
