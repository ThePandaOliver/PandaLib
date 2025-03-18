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

package me.pandamods.pandalib.event.events;

import me.pandamods.pandalib.event.Event;
import me.pandamods.pandalib.event.EventFactory;
import me.pandamods.pandalib.networking.NetworkRegistry;

public interface NetworkingEvents {
	Event<PacketPayloadRegistry> PACKET_PAYLOAD_REGISTRY = EventFactory.create();

	interface PacketPayloadRegistry {
		void register(NetworkRegistry registry);
	}
}
