/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.platform.registration

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.registry.ResourceLoaderRegistryPlatform
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.event.AddServerReloadListenersEvent
import java.util.function.Consumer

@AutoService(ResourceLoaderRegistryPlatform::class)
class ResourceLoaderRegistryImpl : ResourceLoaderRegistryPlatform {
	private val serverDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()
	private val clientDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()

	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: ResourceLocation,
		dependencies: Collection<ResourceLocation>
	) {
		if (packType == PackType.SERVER_DATA) {
			serverDataReloadListeners += id to listener
		} else {
			clientDataReloadListeners += id to listener
		}
	}

	fun addServerReloadListenerEvent(event: AddServerReloadListenersEvent) {
		serverDataReloadListeners.forEach(Consumer { (id, listener) -> event.addListener(id, listener) })
	}

	fun addClientReloadListenerEvent(event: AddClientReloadListenersEvent) {
		clientDataReloadListeners.forEach(Consumer { (id, listener) -> event.addListener(id, listener) })
	}
}