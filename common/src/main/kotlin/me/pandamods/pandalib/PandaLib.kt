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

package me.pandamods.pandalib

import me.pandamods.pandalib.core.event.EventHandler
import me.pandamods.pandalib.core.network.ConfigNetworking
import me.pandamods.pandalib.event.events.NetworkingEvents
import net.minecraft.resources.ResourceLocation

object PandaLib {
	const val MOD_ID = "pandalib"

	@JvmStatic
	fun init() {
		 NetworkingEvents.PACKET_PAYLOAD_REGISTRY.register(ConfigNetworking::registerPackets)

		EventHandler.init()
	}

	@JvmStatic
	fun resourceLocation(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}
}
