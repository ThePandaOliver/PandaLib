/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib

import dev.pandasystems.pandalib.core.event.EventHandler
import dev.pandasystems.pandalib.core.network.ConfigNetworking
import dev.pandasystems.pandalib.event.events.NetworkingEvents
import net.minecraft.resources.ResourceLocation

object PandaLib {
	const val MOD_ID = "pandalib"
	
	fun init() {
		NetworkingEvents.PACKET_PAYLOAD_REGISTRY.register(ConfigNetworking::registerPackets)
		EventHandler.init()
	}
	
	fun resourceLocation(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}
}
