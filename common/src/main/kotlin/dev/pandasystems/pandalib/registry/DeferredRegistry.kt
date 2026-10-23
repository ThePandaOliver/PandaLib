package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.handles.Identifier
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.text.clear

interface DeferredRegistry<T : Any> {
	fun <I : T> register(key: Identifier, provider: (key: ResourceKey<T>) -> I): DeferredObject<I>

	fun finalizeRegistration()

	companion object {
		fun <T : Any> create(registry: Registry<T>): DeferredRegistry<T> =
			create(registry.key())

		fun <T : Any> create(registryKey: ResourceKey<out Registry<T>>): DeferredRegistry<T> =
			PlatformRegistryBridge.create(registryKey)
	}
}