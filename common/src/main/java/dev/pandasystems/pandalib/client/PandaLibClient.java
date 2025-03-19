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

package dev.pandasystems.pandalib.client;

import dev.pandasystems.pandalib.client.resource.AssimpResources;
import dev.pandasystems.pandalib.core.client.event.EventHandlerClient;
import dev.pandasystems.pandalib.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;

public class PandaLibClient {
	private static PandaLibClient instance;
	
	public final AssimpResources assimpResources = new AssimpResources();
	
    public PandaLibClient() {
		instance = this;
		EventHandlerClient.init();

		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, assimpResources);
    }

	public static PandaLibClient getInstance() {
		return instance;
	}
}