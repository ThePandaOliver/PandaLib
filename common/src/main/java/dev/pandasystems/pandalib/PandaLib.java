/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib;

import dev.pandasystems.pandalib.core.event.EventHandler;
import dev.pandasystems.pandalib.core.network.ConfigNetworking;
import dev.pandasystems.pandalib.event.events.NetworkingEvents;
import net.minecraft.resources.ResourceLocation;

public class PandaLib {
    public static final String MOD_ID = "pandalib";
	private static PandaLib instance;

    public PandaLib() {
		instance = this;
		NetworkingEvents.PACKET_PAYLOAD_REGISTRY.register(ConfigNetworking::registerPackets);

		EventHandler.init();
	}

	public static ResourceLocation resourceLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static PandaLib getInstance() {
		return instance;
	}
}