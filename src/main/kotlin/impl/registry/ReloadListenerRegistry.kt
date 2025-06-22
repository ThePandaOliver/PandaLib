package dev.pandasystems.pandalib.impl.registry

import dev.pandasystems.pandalib.api.platform.registryHelper
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
		registryHelper.registerReloadListener(packType, listener, id, dependencies)
	}
}
