/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.registry

//? if fabric {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
//?} elif neoforge {
/*import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.event.AddServerReloadListenersEvent
*///?}

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import java.util.function.Consumer

fun registerResourceLoader(
	packType: PackType,
	listener: PreparableReloadListener,
	id: ResourceLocation
) {
	//? if fabric {
	ResourceLoader.get(packType).registerReloader(id, listener)
	//?} elif neoforge {
	/*if (packType == PackType.SERVER_DATA) {
		serverDataReloadListeners += id to listener
	} else {
		clientDataReloadListeners += id to listener
	}
	*///?}
}

//? if neoforge {
/*private val serverDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()
private val clientDataReloadListeners = mutableListOf<Pair<ResourceLocation, PreparableReloadListener>>()

internal fun addServerReloadListenerEvent(event: AddServerReloadListenersEvent) {
	serverDataReloadListeners.forEach(Consumer { (id, listener) -> event.addListener(id, listener) })
}

internal fun addClientReloadListenerEvent(event: AddClientReloadListenersEvent) {
	clientDataReloadListeners.forEach(Consumer { (id, listener) -> event.addListener(id, listener) })
}
*///?}
