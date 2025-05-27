
package dev.pandasystems.pandalib.platform.services

import dev.pandasystems.pandalib.registry.DeferredObject
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import java.util.function.Supplier

interface RegistrationHelper {
	fun <T> register(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>)
	fun <T> registerNewRegistry(registry: Registry<T>)

	fun registerReloadListener(packType: PackType, listener: PreparableReloadListener, id: ResourceLocation, dependencies: Collection<ResourceLocation>)
}
