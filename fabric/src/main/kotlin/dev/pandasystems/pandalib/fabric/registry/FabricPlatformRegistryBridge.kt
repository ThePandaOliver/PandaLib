package dev.pandasystems.pandalib.fabric.registry

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.core.handles.Identifier
import dev.pandasystems.pandalib.registry.*
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey

@AutoService(PlatformRegistryBridge::class)
class FabricPlatformRegistryBridge : PlatformRegistryBridge {
    override fun <T : Any> create(registryKey: ResourceKey<out Registry<T>>): DeferredRegistry<T> {
        return object : DeferredRegistry<T> {
            private val entries = mutableListOf<Pair<CompletableDeferredObject<T>, () -> T>>()
            private var isRegistered = false

            @Suppress("UNCHECKED_CAST")
            private val targetRegistry: Registry<T> by lazy {
                (BuiltInRegistries.REGISTRY.getValue(registryKey.identifier()) as? Registry<T>)
                    ?: error("Registry '${registryKey.identifier()}' not found in BuiltInRegistries.")
            }

            override fun <I : T> register(key: Identifier, provider: (key: ResourceKey<T>) -> I): DeferredObject<I> {
                val obj = CompletableDeferredObject<I>(key)
                val supplier: () -> T = {
	                provider(ResourceKey.create(registryKey, key)).also { obj.bind(it) } as T
                }

                if (isRegistered) {
                    Registry.register(targetRegistry, key, supplier())
                } else {
                    @Suppress("UNCHECKED_CAST")
                    entries.add((obj to supplier) as Pair<CompletableDeferredObject<T>, () -> T>)
                }

                return obj
            }

            override fun finalizeRegistration() {
                if (isRegistered) return
                isRegistered = true
                entries.forEach { (obj, supplier) ->
                    Registry.register(targetRegistry, obj.key, supplier())
                }
                entries.clear()
            }
        }
    }
}