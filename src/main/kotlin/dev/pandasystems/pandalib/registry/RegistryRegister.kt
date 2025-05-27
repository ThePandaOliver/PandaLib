
package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.platform.Services
import net.minecraft.core.Registry

@Suppress("unused")
object RegistryRegister {
	@JvmStatic
	fun <T> register(registry: Registry<T>): Registry<T> {
		Services.REGISTRATION.registerNewRegistry<T>(registry)
		return registry
	}
}
