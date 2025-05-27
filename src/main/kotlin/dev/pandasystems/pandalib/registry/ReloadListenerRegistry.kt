
package dev.pandasystems.pandalib.registry

import dev.pandasystems.pandalib.platform.Services
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener

object ReloadListenerRegistry {
	@JvmOverloads
	@JvmStatic
	fun register(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: MutableList<ResourceLocation> = mutableListOf()
	) {
		Services.REGISTRATION.registerReloadListener(packType, listener, id, dependencies)
	}
}
