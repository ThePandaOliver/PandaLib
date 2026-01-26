/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.neoforge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.ResourceLoaderRegistryPlatform
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ReloadableResourceManager
import net.neoforged.neoforge.event.AddReloadListenerEvent
import java.util.function.Consumer


@AutoService(ResourceLoaderRegistryPlatform::class)
class ResourceLoaderRegistryImpl : ResourceLoaderRegistryPlatform {
	private val serverDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()

	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: Collection<ResourceLocation>
	) {
		if (packType == PackType.SERVER_DATA) {
			serverDataReloadListeners += id to listener
		} else {
			(Minecraft.getInstance().resourceManager as ReloadableResourceManager).registerReloadListener(listener)
		}
	}

	fun addServerReloadListenerEvent(event: AddReloadListenerEvent) {
		serverDataReloadListeners.forEach(Consumer { (_, listener) -> event.addListener(listener) })
	}
}