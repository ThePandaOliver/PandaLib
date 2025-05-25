/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.pandasystems.pandalib.event.events

import dev.architectury.event.Event
import dev.architectury.event.EventFactory
import dev.pandasystems.pandalib.networking.NetworkRegistry

interface NetworkingEvents {
	fun interface PacketPayloadRegistry {
		fun register(registry: NetworkRegistry)
	}

	companion object {
		@JvmField
		val PACKET_PAYLOAD_REGISTRY: Event<PacketPayloadRegistry> = EventFactory.createLoop<PacketPayloadRegistry>()
	}
}
