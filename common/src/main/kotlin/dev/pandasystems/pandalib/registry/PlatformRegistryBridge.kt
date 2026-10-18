package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.core.utils.loadService
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

interface PlatformRegistryBridge {
    fun <T : Any> create(registryKey: ResourceKey<out Registry<T>>): DeferredRegistry<T>

    companion object: PlatformRegistryBridge by loadService()
}