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
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.event.AddServerReloadListenersEvent
import java.util.function.Consumer

@AutoService(ResourceLoaderRegistryPlatform::class)
class ResourceLoaderRegistryImpl : ResourceLoaderRegistryPlatform {
	private val serverDataReloadListeners = mutableListOf<Pair<Identifier, PreparableReloadListener>>()
	private val clientDataReloadListeners = mutableListOf<Pair<Identifier, PreparableReloadListener>>()

	override fun registerReloadListener(
		packType: PackType,
		listener: PreparableReloadListener,
		id: Identifier,
		dependencies: Collection<Identifier>
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