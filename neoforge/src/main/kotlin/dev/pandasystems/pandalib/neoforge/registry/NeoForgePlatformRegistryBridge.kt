package dev.pandasystems.pandalib.neoforge.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.registry.*
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegisterEvent
import java.util.concurrent.ConcurrentHashMap

@AutoService(PlatformRegistryBridge::class)
class NeoForgePlatformRegistryBridge : PlatformRegistryBridge {
	override fun <T : Any> create(registryKey: ResourceKey<out Registry<T>>): DeferredRegistry<T> {
		return object : DeferredRegistry<T> {
			override fun register(key: Identifier, provider: (key: ResourceKey<T>) -> T): DeferredObject<T> {
				val obj = CompletableDeferredObject<T>(key)
				@Suppress("UNCHECKED_CAST")
				queue(registryKey, key, obj, provider)
				return obj
			}

			override fun finalizeRegistration() {
				// No-op or touch point: classloading has occurred
			}
		}
	}

	internal class RegistrationEntry<T : Any>(
		val key: Identifier,
		val obj: CompletableDeferredObject<T>,
		val provider: (ResourceKey<T>) -> T
	)

	companion object {
		private val registries = ConcurrentHashMap<ResourceKey<out Registry<*>>, MutableList<RegistrationEntry<*>>>()

		internal fun <T : Any> queue(
			registryKey: ResourceKey<out Registry<T>>,
			key: Identifier,
			obj: CompletableDeferredObject<T>,
			provider: (ResourceKey<T>) -> T
		) {
			val list = registries.computeIfAbsent(registryKey) { mutableListOf() }
			synchronized(list) {
				list.add(RegistrationEntry(key, obj, provider))
			}
		}

		@Suppress("UNCHECKED_CAST")
		internal fun onRegisterEvent(event: RegisterEvent) {
			val entries = registries[event.registryKey] ?: return
			synchronized(entries) {
				for (entry in entries) {
					val typedEntry = entry as RegistrationEntry<Any>
					event.register(event.registryKey as ResourceKey<out Registry<Any>>, typedEntry.key) {
						typedEntry.provider(ResourceKey.create(event.registryKey as ResourceKey<out Registry<Any>>, typedEntry.key)).also { bound ->
							typedEntry.obj.bind(bound)
						}
					}
				}
				entries.clear()
			}
		}
	}
}